import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { MessagesService, Message, ConversationTopicDTO } from 'src/app/services/messages/messages.service';
import { UserService, UserResponse } from 'src/app/services/user/user.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  messages: Message[] = [];
  users: UserResponse[] = [];
  selectedUserId: number | null = null;
  selectedUserName: string = '';
  // ID do utilizador autenticado.  Inicialmente `null` até sabermos
  // com certeza quem é, porque o JWT pode não conter um número.
  myUserId: number | null = null;
  myUserName: string = 'Eu';

  newMessage: string = '';
  isLoading: boolean = false;
  errorMessage: string = '';
  isConnected: boolean = false;

  private destroy$ = new Subject<void>();
  private scrollToBottom = false;

  constructor(
    private messagesService: MessagesService,
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    try {
      this.loadUsers();
      this.initializeWebSocket();
      this.subscribeToMessages();
      this.subscribeToConnectionStatus();
    } catch (err) {
      console.error('Erro em ngOnInit do ChatComponent:', err);
      this.errorMessage = 'Erro inesperado ao inicializar chat';
    }
  }

  ngAfterViewChecked(): void {
    if (this.scrollToBottom) {
      this.scrollMessagesDown();
      this.scrollToBottom = false;
    }
  }

  ngOnDestroy(): void {
    this.messagesService.unsubscribeFromConversation();
    this.messagesService.disconnectWebSocket();
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Inicializa a conexão WebSocket
   */
  private initializeWebSocket(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      this.errorMessage = 'Token não disponível. Por favor, faça login novamente.';
      return;
    }

    this.messagesService.connectWebSocket(token)
      .then(() => {
        console.log('WebSocket conectado com sucesso');
      })
      .catch((err) => {
        this.errorMessage = 'Erro ao conectar ao servidor de mensagens';
        console.error('Erro ao conectar WebSocket:', err);
      });
  }

  /**
   * Subscreve ao status de conexão do WebSocket
   */
  private subscribeToConnectionStatus(): void {
    this.messagesService.connected$
      .pipe(takeUntil(this.destroy$))
      .subscribe((connected) => {
        this.isConnected = connected;
      });
  }

  /**
   * Subscreve a novas mensagens via WebSocket
   */
  private subscribeToMessages(): void {
    this.messagesService.newMessage$
      .pipe(takeUntil(this.destroy$))
      .subscribe((message) => {
        // Adiciona a mensagem ao stream
        if (!this.messages.find(m => m.id === message.id)) {
          this.addMessageToList(message);
          this.scrollToBottom = true;
        }
      });
  }

  /**
   * Carrega lista de utilizadores
   */
  loadUsers(): void {
    this.userService.getUsers().subscribe({
      next: (res: any) => {
        const allUsers: UserResponse[] = res.data;

        // decode token once; may be null or contain strings
        const tokenUser: any = this.authService.getUserFromToken();
        let tokenEmail: string | null = null;
        let tokenNumericId: number | null = null;

        if (tokenUser) {
          // preferentially use numeric id claim if present
          const rawId = tokenUser.id ?? tokenUser.sub ?? null;
          if (rawId != null) {
            const parsed = Number(rawId);
            if (!isNaN(parsed)) {
              tokenNumericId = parsed;
            }
          }

          // tokens vary; try several common claim names for the user’s email
          tokenEmail =
            typeof tokenUser.mail === 'string' ? tokenUser.mail :
            typeof tokenUser.email === 'string' ? tokenUser.email :
            typeof tokenUser.sub === 'string' ? tokenUser.sub :
            typeof tokenUser.username === 'string' ? tokenUser.username :
            typeof tokenUser.preferred_username === 'string' ? tokenUser.preferred_username :
            null;

          if (tokenUser.name) {
            this.myUserName = tokenUser.name;
          }
        }

        // determine myUserId using whatever information we have
        if (tokenNumericId != null) {
          this.myUserId = tokenNumericId;
        } else if (tokenEmail) {
          const lower = tokenEmail.toLowerCase();
          const me = allUsers.find(u => u.mail.toLowerCase() === lower);
          if (me) {
            this.myUserId = me.id;
            this.myUserName = `${me.nom} ${me.prenom}`;
          }
        }

        if (this.myUserId == null) {
          console.warn('Não foi possível determinar ID do utilizador a partir do token. ' +
                       'A lista não será filtrada e o histórico poderá estar incorreto.');
        }

        // filter out self if known
        this.users = this.myUserId != null ?
          allUsers.filter(u => u.id !== this.myUserId) :
          allUsers.slice();

        if (this.users.length > 0) {
          this.selectedUserId = this.users[0].id;
          this.selectedUserName = `${this.users[0].nom} ${this.users[0].prenom}`;
          this.loadConversation();
        }
      },
      error: (err) => {
        this.errorMessage = 'Erro ao carregar utilizadores';
        console.error('Erro ao carregar utilizadores:', err);
      }
    });
  }

  /**
   * Muda de utilizador
   */
  onUserChange(): void {
    if (this.selectedUserId) {
      const selectedUser = this.users.find(u => u.id === this.selectedUserId);
      if (selectedUser) {
        this.selectedUserName = `${selectedUser.nom} ${selectedUser.prenom}`;
      }
      
      this.messages = [];
      this.messagesService.unsubscribeFromConversation();
      this.loadConversation();
    }
  }

  /**
   * Carrega histórico de conversa
   */
  loadConversation(): void {
    if (!this.selectedUserId) return;
    if (this.myUserId == null) {
      console.error('ID do usuário desconhecido, não é possível carregar conversa');
      return;
    }

    console.debug('loadConversation para', { myUserId: this.myUserId, other: this.selectedUserId });

    this.isLoading = true;
    
    // Carrega histórico via REST
    this.messagesService.getConversation(this.selectedUserId).subscribe({
      next: (msgs) => {
        this.messages = msgs.map(msg => ({
          ...msg,
          timestamp: msg.timestamp ? new Date(msg.timestamp) : new Date()
        }));
        this.messagesService.updateMessages(this.messages);
        this.isLoading = false;
        this.scrollToBottom = true;

        // Subscreve ao tópico de conversa para receber mensagens em tempo real
        if (this.isConnected && this.selectedUserId && this.myUserId != null) {
          this.messagesService.subscribeToConversation(this.myUserId, this.selectedUserId);
        }

        // Marca todas as mensagens recebidas como lidas
        this.markConversationAsRead();
      },
      error: (err) => {
        this.errorMessage = 'Erro ao carregar histórico';
        console.error('Erro ao carregar conversa:', err);
        this.isLoading = false;
      }
    });
  }

  /**
   * Marca todos os mensagens da conversa como lidas
   */
  private markConversationAsRead(): void {
    this.messages
      .filter(msg => msg.receiverId === this.myUserId && !msg.read)
      .forEach(msg => {
        if (msg.id) {
          this.messagesService.markMessageAsRead(msg.id).subscribe({
            error: (err) => console.error('Erro ao marcar como lido:', err)
          });
        }
      });
  }

  /**
   * Envia mensagem
   */
  sendMessage(): void {
    if (!this.newMessage.trim() || !this.selectedUserId) return;
    if (this.myUserId == null) {
      console.error('Tentativa de enviar mensagem sem ID de usuário definido');
      this.errorMessage = 'Erro interno: usuário desconhecido';
      return;
    }

    if (!this.isConnected) {
      this.errorMessage = 'Desconectado. Enviando via HTTP...';
      this.sendMessageViaRest();
      return;
    }

    const messagePayload: ConversationTopicDTO = {
      senderId: this.myUserId!, // guarded above
      receiverId: this.selectedUserId,
      content: this.newMessage.trim()
    };

    try {
      console.log('enviando mensagem via WebSocket', messagePayload);
      this.messagesService.sendMessageWebSocket(messagePayload);
      this.newMessage = '';
      this.errorMessage = '';
    } catch (err) {
      console.error('Erro ao enviar via WebSocket:', err);
      this.sendMessageViaRest();
    }
  }

  /**
   * Envia mensagem via REST como fallback
   */
  private sendMessageViaRest(): void {
    if (!this.selectedUserId) return;

    const msg: Message = {
      // make extra sure we are sending numbers; the template types are
      // numeric but the values may have been set from untyped sources
      senderId: Number(this.myUserId),
      receiverId: Number(this.selectedUserId),
      content: this.newMessage.trim(),
      senderName: this.myUserName
    };

    this.messagesService.sendMessageRest(msg).subscribe({
      next: (savedMsg) => {
        this.addMessageToList(savedMsg);
        this.newMessage = '';
        this.errorMessage = '';
        this.scrollToBottom = true;
      },
      error: (err) => {
        this.errorMessage = 'Erro ao enviar mensagem';
        console.error('Erro ao enviar mensagem:', err);
      }
    });
  }

  /**
   * Adiciona mensagem à lista local
   */
  private addMessageToList(message: Message): void {
    this.messages.push({
      ...message,
      timestamp: message.timestamp ? new Date(message.timestamp) : new Date()
    });
  }

  /**
   * Realiza scroll até o fim do container de mensagens
   */
  private scrollMessagesDown(): void {
    try {
      this.messagesContainer.nativeElement.scrollTop = 
        this.messagesContainer.nativeElement.scrollHeight;
    } catch (err) {
      console.log('Erro ao fazer scroll:', err);
    }
  }

  /**
   * Verifica se mensagem é do utilizador atual
   */
  isMyMessage(senderId: number): boolean {
    return senderId === this.myUserId;
  }

  /**
   * Formata a hora da mensagem
   */
  formatTime(timestamp: Date | string | undefined): string {
    if (!timestamp) return '';
    const date = typeof timestamp === 'string' ? new Date(timestamp) : timestamp;
    return date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
  }

  /**
   * Limpa mensagem de erro
   */
  clearError(): void {
    this.errorMessage = '';
  }
}
