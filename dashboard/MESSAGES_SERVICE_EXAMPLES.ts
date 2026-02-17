/**
 * Exemplos de integração com o MessagesService
 * Copie e adapte conforme necessário
 */

// EXEMPLO 1: Serviço genérico de notificações de mensagens
// =========================================================

import { Injectable } from '@angular/core';
import { MessagesService, Message } from 'src/app/services/messages/messages.service';
import { ToastrService } from 'ngx-toastr'; // ou seu toast service
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private destroy$ = new Subject<void>();
  private unreadCount = 0;

  constructor(
    private messagesService: MessagesService,
    private toastr: ToastrService
  ) {
    this.setupNotifications();
  }

  private setupNotifications() {
    // Subscreve a novas mensagens
    this.messagesService.newMessage$
      .pipe(takeUntil(this.destroy$))
      .subscribe(message => {
        this.unreadCount++;
        
        // Mostrar toast
        this.toastr.info(
          `Nova mensagem de ${message.senderName}`,
          'Nova Mensagem',
          {
            timeOut: 5000,
            progressBar: true
          }
        );

        // Atualizar badge (se houver)
        this.updateUnreadBadge(this.unreadCount);

        // Som de notificação (opcional)
        this.playNotificationSound();
      });
  }

  private updateUnreadBadge(count: number) {
    // Implementar atualização de badge
    // Exemplo: atualizar title da aba
    if (count > 0) {
      document.title = `(${count}) Mensagens`;
    }
  }

  private playNotificationSound() {
    // Implementar som de notificação
    const audio = new Audio('assets/notification.mp3');
    audio.play().catch(err => console.log('Erro ao tocar som:', err));
  }

  getUnreadCount(): number {
    return this.unreadCount;
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// EXEMPLO 2: Componente com lista de conversas ativas
// ===================================================

import { Component, OnInit, OnDestroy } from '@angular/core';
import { MessagesService, Message } from 'src/app/services/messages/messages.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

interface ConversationPreview {
  userId: number;
  userName: string;
  lastMessage: string;
  lastMessageTime: Date;
  unreadCount: number;
  isOnline: boolean;
}

@Component({
  selector: 'app-conversations-list',
  template: `
    <div class="conversations">
      <div class="connection-status">
        {{ isConnected ? '🟢 Conectado' : '🔴 Desconectado' }}
      </div>
      
      <div class="conversation-item" 
           *ngFor="let conv of conversations"
           (click)="selectConversation(conv)"
           [class.active]="selectedConvId === conv.userId">
        <div class="avatar">{{ conv.userName.charAt(0) }}</div>
        <div class="info">
          <div class="name">{{ conv.userName }}</div>
          <div class="last-message">{{ conv.lastMessage }}</div>
        </div>
        <div class="meta">
          <span class="time">{{ formatTime(conv.lastMessageTime) }}</span>
          <span *ngIf="conv.unreadCount > 0" class="badge">
            {{ conv.unreadCount }}
          </span>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .conversations { padding: 10px; }
    .connection-status { padding: 10px; text-align: center; font-size: 12px; }
    .conversation-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 10px;
      margin: 5px 0;
      border-radius: 8px;
      cursor: pointer;
      transition: background 0.2s;
    }
    .conversation-item:hover { background: #f0f0f0; }
    .conversation-item.active { background: #667eea; color: white; }
    .avatar { width: 40px; height: 40px; border-radius: 50%; background: #ddd; display: flex; align-items: center; justify-content: center; }
    .info { flex: 1; min-width: 0; }
    .name { font-weight: 600; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    .last-message { font-size: 12px; color: #666; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    .meta { display: flex; flex-direction: column; align-items: flex-end; gap: 5px; }
    .badge { background: #ff6b6b; color: white; border-radius: 50%; width: 24px; height: 24px; display: flex; align-items: center; justify-content: center; font-size: 12px; }
  `]
})
export class ConversationsListComponent implements OnInit, OnDestroy {
  conversations: ConversationPreview[] = [];
  selectedConvId: number | null = null;
  isConnected = false;
  private destroy$ = new Subject<void>();
  private messageMap: Map<number, Message[]> = new Map();

  constructor(
    private messagesService: MessagesService,
    private userService: UserService
  ) {}

  ngOnInit() {
    // Monitorar conexão
    this.messagesService.connected$
      .pipe(takeUntil(this.destroy$))
      .subscribe(connected => this.isConnected = connected);

    // Monitorar novas mensagens
    this.messagesService.newMessage$
      .pipe(takeUntil(this.destroy$))
      .subscribe(msg => this.updateConversationPreview(msg));

    this.loadConversations();
  }

  private loadConversations() {
    // Carregar lista de conversas com último preview
    this.userService.getUsers().subscribe(users => {
      this.conversations = users.map(u => ({
        userId: u.id,
        userName: `${u.nom} ${u.prenom}`,
        lastMessage: 'Sem mensagens',
        lastMessageTime: new Date(),
        unreadCount: 0,
        isOnline: false
      }));
    });
  }

  private updateConversationPreview(msg: Message) {
    const conv = this.conversations.find(
      c => c.userId === msg.senderId || c.userId === msg.receiverId
    );
    if (conv) {
      conv.lastMessage = msg.content.substring(0, 30) + (msg.content.length > 30 ? '...' : '');
      conv.lastMessageTime = new Date(msg.timestamp || new Date());
      if (!msg.read && msg.receiverId === this.getCurrentUserId()) {
        conv.unreadCount++;
      }
    }
  }

  selectConversation(conv: ConversationPreview) {
    this.selectedConvId = conv.userId;
    // Disparar evento ou navegação
  }

  formatTime(date: Date): string {
    const now = new Date();
    const diff = now.getTime() - date.getTime();
    
    if (diff < 60000) return 'Agora';
    if (diff < 3600000) return `${Math.floor(diff / 60000)}m`;
    if (diff < 86400000) return date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    return date.toLocaleDateString('pt-BR');
  }

  private getCurrentUserId(): number {
    return parseInt(localStorage.getItem('userId') || '1', 10);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// EXEMPLO 3: Interceptor de erro para reconexão automática
// ========================================================

import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private messagesService: MessagesService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      retry(1),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 0) {
          // Erro de conexão - tentar reconectar WebSocket
          const token = localStorage.getItem('token');
          if (token && !this.messagesService.isConnected()) {
            this.messagesService.connectWebSocket(token)
              .catch(err => console.error('Erro ao reconectar:', err));
          }
        }
        return throwError(() => error);
      })
    );
  }
}

// EXEMPLO 4: Guard para inicializar WebSocket na rota
// ==================================================

import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { MessagesService } from 'src/app/services/messages/messages.service';

@Injectable({
  providedIn: 'root'
})
export class ChatGuard implements CanActivate {
  constructor(
    private messagesService: MessagesService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean> {
    const token = localStorage.getItem('token');
    
    if (!token) {
      this.router.navigate(['/login']);
      return Promise.resolve(false);
    }

    return this.messagesService.connectWebSocket(token)
      .then(() => true)
      .catch(err => {
        console.error('Erro ao inicializar chat:', err);
        return false;
      });
  }
}

// Usar na rota:
// { path: 'chat', component: ChatComponent, canActivate: [ChatGuard] }

// EXEMPLO 5: Stateless service para analisar conversas
// ==================================================

import { Injectable } from '@angular/core';
import { MessagesService, Message } from 'src/app/services/messages/messages.service';
import { Observable, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ConversationAnalyzerService {
  constructor(private messagesService: MessagesService) {}

  /**
   * Obtém estatísticas de conversa
   */
  getConversationStats(userId: number): Observable<{
    totalMessages: number;
    unreadMessages: number;
    averageResponseTime: number;
  }> {
    return forkJoin({
      conversation: this.messagesService.getConversation(userId),
      unread: this.messagesService.getUnreadMessages(userId)
    }).pipe(
      map(({ conversation, unread }) => ({
        totalMessages: conversation.length,
        unreadMessages: unread.length,
        averageResponseTime: this.calculateAverageResponseTime(conversation)
      }))
    );
  }

  /**
   * Pesquisa em mensagens
   */
  searchMessages(keyword: string, conversations: Message[]): Message[] {
    return conversations.filter(msg =>
      msg.content.toLowerCase().includes(keyword.toLowerCase())
    );
  }

  private calculateAverageResponseTime(messages: Message[]): number {
    // Implementar lógica de cálculo
    return 0;
  }
}
