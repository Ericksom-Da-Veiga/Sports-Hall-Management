import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, Subject } from 'rxjs';
import { Client, IStompSocket } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';

export interface Message {
  id?: number;
  senderId: number;
  receiverId: number;
  content: string;
  timestamp?: Date | string;
  senderName?: string;
  status?: 'SENT' | 'DELIVERED' | 'READ';
  read?: boolean;
}

export interface ConversationTopicDTO {
  senderId: number;
  receiverId: number;
  content: string;
}

@Injectable({
  providedIn: 'root'
})
export class MessagesService {
  private apiUrl = 'http://localhost:8080/messages';
  private wsUrl = 'http://localhost:8080/ws';
  
  private stompClient: Client | null = null;
  private messagesSubject = new BehaviorSubject<Message[]>([]);
  public messages$ = this.messagesSubject.asObservable();
  
  private connectedSubject = new BehaviorSubject<boolean>(false);
  public connected$ = this.connectedSubject.asObservable();
  
  private newMessageSubject = new Subject<Message>();
  public newMessage$ = this.newMessageSubject.asObservable();
  
  private currentConversationTopic: string = '';
  private currentSubscription: import('@stomp/stompjs').StompSubscription | null = null; // guarda a subscrição STOMP

  constructor(private http: HttpClient) {}

  /**
   * Conecta ao servidor WebSocket
   */
  connectWebSocket(token: string): Promise<void> {
    return new Promise((resolve, reject) => {
      if (this.stompClient?.active) {
        resolve();
        return;
      }

      try {
        this.stompClient = new Client({
          webSocketFactory: () => {
            if (typeof SockJS !== 'function') {
              throw new Error('SockJS não está disponível');
            }
            return new (SockJS as any)(this.wsUrl) as IStompSocket;
          },
          connectHeaders: {
            Authorization: `Bearer ${token}`
          },
          debug: (str) => {
            console.log('[STOMP]', str);
          },
          reconnectDelay: 5000,
          heartbeatIncoming: 4000,
          heartbeatOutgoing: 4000,
        });
      } catch (err) {
        console.error('Erro ao criar cliente STOMP:', err);
        reject(err);
        return;
      }

      this.stompClient.onConnect = () => {
        console.log('Conectado ao WebSocket');
        this.connectedSubject.next(true);
        resolve();
      };

      this.stompClient.onStompError = (frame) => {
        console.error('Erro STOMP:', frame);
        this.connectedSubject.next(false);
        reject(frame);
      };

      try {
        this.stompClient.activate();
      } catch (err) {
        console.error('Falha ao ativar STOMP client:', err);
        reject(err);
      }
    });
  }

  /**
   * Desconecta do WebSocket
   */
  disconnectWebSocket(): void {
    if (this.stompClient?.active) {
      this.stompClient.deactivate();
      this.connectedSubject.next(false);
      this.currentConversationTopic = '';
    }
  }

  /**
   * Obtém histórico da conversa entre dois utilizadores
   */
  getConversation(otherUserId: number): Observable<Message[]> {
    return this.http.get<Message[]>(
      `${this.apiUrl}/conversation/${otherUserId}`
    );
  }

  /**
   * Obtém histórico genérico entre dois utilizadores
   */
  getConversationGeneric(user1: number, user2: number): Observable<Message[]> {
    return this.http.get<Message[]>(
      `${this.apiUrl}/conversation?user1_id=${user1}&user2_id=${user2}`
    );
  }

  /**
   * Obtém mensagens não lidas para um utilizador
   */
  getUnreadMessages(receiverId: number): Observable<Message[]> {
    return this.http.get<Message[]>(
      `${this.apiUrl}/unread/${receiverId}`
    );
  }

  /**
   * Marca uma mensagem como lida
   */
  markMessageAsRead(messageId: number): Observable<any> {
    return this.http.put(
      `${this.apiUrl}/${messageId}/read`,
      {}
    );
  }

  /**
   * Envia mensagem via REST (fallback)
   */
  sendMessageRest(message: Message): Observable<Message> {
    return this.http.post<Message>(this.apiUrl, message);
  }

  /**
   * Envia mensagem via WebSocket
   */
  sendMessageWebSocket(message: ConversationTopicDTO): void {
    if (!this.stompClient?.active) {
      console.error('WebSocket não conectado');
      return;
    }

    console.debug('publicando mensagem via WebSocket:', message);
    this.stompClient.publish({
      destination: '/app/chat',
      body: JSON.stringify(message)
    });
  }

  /**
   * Subscreve a um tópico de conversa
   */
  subscribeToConversation(user1: number, user2: number): void {
    if (!this.stompClient?.active) {
      console.error('WebSocket não conectado');
      return;
    }

    // Gera tópico com IDs ordenados
    const minId = Math.min(user1, user2);
    const maxId = Math.max(user1, user2);
    this.currentConversationTopic = `/topic/conversation.${minId}.${maxId}`;

    // Desinscreve do tópico anterior se houver
    if (this.currentSubscription) {
      this.currentSubscription.unsubscribe();
      this.currentSubscription = null;
    }

    // Subscreve ao novo tópico
    this.currentSubscription = this.stompClient.subscribe(
      this.currentConversationTopic,
      (message) => {
        try {
          const receivedMessage: Message = JSON.parse(message.body);
          this.newMessageSubject.next(receivedMessage);
          
          const currentMessages = this.messagesSubject.value;
          this.messagesSubject.next([...currentMessages, receivedMessage]);
        } catch (e) {
          console.error('Erro ao parsear mensagem:', e);
        }
      }
    );

    console.log('Inscrito ao tópico:', this.currentConversationTopic);
  }

  /**
   * Desinscreve do tópico atual
   */
  unsubscribeFromConversation(): void {
    if (this.currentSubscription) {
      this.currentSubscription.unsubscribe();
      this.currentSubscription = null;
    }
    this.currentConversationTopic = '';
  }

  /**
   * Atualiza mensagens localmente
   */
  updateMessages(messages: Message[]): void {
    this.messagesSubject.next(messages);
  }

  /**
   * Adiciona uma mensagem ao stream
   */
  addMessage(message: Message): void {
    const currentMessages = this.messagesSubject.value;
    this.messagesSubject.next([...currentMessages, message]);
  }

  /**
   * Retorna o stream de mensagens
   */
  getMessagesStream(): Observable<Message[]> {
    return this.messages$;
  }

  /**
   * Verifica se WebSocket está conectado
   */
  isConnected(): boolean {
    return this.stompClient?.active ?? false;
  }
}
