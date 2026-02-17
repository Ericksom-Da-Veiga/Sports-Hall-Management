/**
 * Tipos e Interfaces para o Sistema de Mensagens
 * 
 * Este arquivo documenta todas as estruturas de dados utilizadas
 * no sistema de mensagens frontend/backend
 */

/**
 * Mensagem Principal
 * Representa uma mensagem no sistema
 */
export interface Message {
  /** ID único da mensagem (gerado pelo backend) */
  id?: number;

  /** ID do utilizador que enviou a mensagem */
  senderId: number;

  /** ID do utilizador que recebeu a mensagem */
  receiverId: number;

  /** Conteúdo da mensagem */
  content: string;

  /** Timestamp da mensagem (ISO 8601) */
  timestamp?: Date | string;

  /** Nome do remetente (para exibição) */
  senderName?: string;

  /** Status da mensagem */
  status?: MessageStatus;

  /** Se foi marcada como lida */
  read?: boolean;

  /** Metadados opcionais */
  metadata?: Record<string, any>;
}

/**
 * Status possível de uma mensagem
 */
export type MessageStatus = 'SENT' | 'DELIVERED' | 'READ';

/**
 * Payload enviado via WebSocket para criar nova mensagem
 */
export interface ConversationTopicDTO {
  /** ID do remetente */
  senderId: number;

  /** ID do destinatário */
  receiverId: number;

  /** Conteúdo da mensagem */
  content: string;
}

/**
 * Resposta de histórico de conversa
 */
export interface ConversationHistory {
  /** ID da primeira conversa */
  userId1: number;

  /** ID da segunda conversa */
  userId2: number;

  /** Lista de mensagens */
  messages: Message[];

  /** Total de mensagens */
  totalCount: number;

  /** Página atual (se paginado) */
  page?: number;

  /** Tamanho da página (se paginado) */
  pageSize?: number;
}

/**
 * Resposta com mensagens não lidas
 */
export interface UnreadMessagesResponse {
  /** ID do utilizador para o qual as mensagens não foram lidas */
  receiverId: number;

  /** Lista de mensagens não lidas */
  messages: Message[];

  /** Total de mensagens não lidas */
  totalCount: number;
}

/**
 * Resultado de operação REST
 */
export interface ApiResponse<T> {
  /** Indicador de sucesso */
  success: boolean;

  /** Mensagem de resposta */
  message?: string;

  /** Dados da resposta */
  data?: T;

  /** Código de erro (se houver) */
  errorCode?: string;

  /** Timestamp da resposta */
  timestamp?: string;
}

/**
 * Configuração de conexão WebSocket
 */
export interface WebSocketConfig {
  /** URL do endpoint WebSocket */
  url: string;

  /** Token JWT para autenticação */
  token: string;

  /** Delay antes de reconexão (ms) */
  reconnectDelay?: number;

  /** Heartbeat incoming (ms) */
  heartbeatIncoming?: number;

  /** Heartbeat outgoing (ms) */
  heartbeatOutgoing?: number;

  /** Enable debug logging */
  debug?: boolean;
}

/**
 * Evento de status de conexão
 */
export interface ConnectionStatusEvent {
  /** Se está conectado */
  connected: boolean;

  /** Timestamp do evento */
  timestamp: Date;

  /** Motivo da mudança */
  reason?: string;

  /** Tentativa de reconexão */
  reconnectAttempt?: number;
}

/**
 * Configuração de preferências de mensagens
 */
export interface MessagePreferences {
  /** Notificar novas mensagens */
  enableNotifications?: boolean;

  /** Som de notificação */
  enableSound?: boolean;

  /** Mostrar preview de mensagem */
  showPreview?: boolean;

  /** Salvar histórico localmente */
  saveHistory?: boolean;

  /** Limite de histórico local (dias) */
  historyLimit?: number;
}

/**
 * Filtros para busca de mensagens
 */
export interface MessageFilters {
  /** Palavra-chave a buscar */
  keyword?: string;

  /** Buscar entre estes utilizadores */
  conversationWith?: number[];

  /** Data inicial */
  startDate?: Date;

  /** Data final */
  endDate?: Date;

  /** Estado de leitura */
  readStatus?: 'read' | 'unread' | 'all';

  /** Limite de resultados */
  limit?: number;

  /** Offset para paginação */
  offset?: number;
}

/**
 * Resultado de busca de mensagens
 */
export interface MessageSearchResult {
  /** Mensagens encontradas */
  messages: Message[];

  /** Total de resultados */
  totalResults: number;

  /** Página atual */
  page: number;

  /** Total de páginas */
  totalPages: number;
}

/**
 * Análise de conversa
 */
export interface ConversationAnalytics {
  /** ID do utilizador da conversa */
  userId: number;

  /** Total de mensagens */
  totalMessages: number;

  /** Mensagens enviadas pelo utilizador local */
  sentMessages: number;

  /** Mensagens recebidas */
  receivedMessages: number;

  /** Total de mensagens não lidas */
  unreadMessages: number;

  /** Última mensagem */
  lastMessage?: Message;

  /** Data da primeira mensagem */
  firstMessageDate?: Date;

  /** Data da última mensagem */
  lastMessageDate?: Date;

  /** Tempo médio de resposta (segundos) */
  averageResponseTime?: number;
}

/**
 * Tópico STOMP
 */
export enum StompTopics {
  /** Formato: /topic/conversation.{minId}.{maxId} */
  CONVERSATION = '/topic/conversation',

  /** Formato: /topic/user.{userId}.notifications */
  USER_NOTIFICATIONS = '/topic/user',

  /** Formato: /topic/system.notifications */
  SYSTEM = '/topic/system'
}

/**
 * Endpoints STOMP
 */
export enum StompEndpoints {
  /** POST para enviar mensagem */
  SEND_MESSAGE = '/app/chat',

  /** GET para obter status */
  GET_STATUS = '/app/status',

  /** POST para marcar como lida */
  MARK_READ = '/app/messages'
}

/**
 * Endpoints REST
 */
export enum RestEndpoints {
  /** GET/POST histórico */
  CONVERSATION = '/messages/conversation',

  /** GET não-lidas */
  UNREAD = '/messages/unread',

  /** PUT marcar como lida */
  MARK_READ = '/messages',

  /** GET todos os endpoints base */
  BASE = '/messages'
}

/**
 * Erros possíveis
 */
export enum MessageErrors {
  INVALID_RECIPIENT = 'INVALID_RECIPIENT',
  MESSAGE_TOO_LONG = 'MESSAGE_TOO_LONG',
  RATE_LIMITED = 'RATE_LIMITED',
  UNAUTHORIZED = 'UNAUTHORIZED',
  WEBSOCKET_ERROR = 'WEBSOCKET_ERROR',
  HTTP_ERROR = 'HTTP_ERROR',
  CONNECTION_TIMEOUT = 'CONNECTION_TIMEOUT'
}

/**
 * Evento de digitação
 */
export interface TypingEvent {
  /** ID do utilizador que está digitando */
  userId: number;

  /** ID do utilizador receptor */
  recipientId: number;

  /** Se está digitando ou parou */
  isTyping: boolean;

  /** Timestamp */
  timestamp: Date;
}

/**
 * Estado da conversa
 */
export interface ConversationState {
  /** Mensagens carregadas */
  messages: Message[];

  /** Utilizador selecionado */
  selectedUserId: number | null;

  /** Conectado ao WebSocket */
  isConnected: boolean;

  /** Carregando histórico */
  isLoading: boolean;

  /** Erro atual */
  error: string | null;

  /** Página atual do histórico */
  page: number;

  /** Total de páginas */
  totalPages: number;

  /** Usuário no topo não está visível (para scroll infinito) */
  hasMoreMessages: boolean;
}

/**
 * Configuração de paginação
 */
export interface PaginationConfig {
  /** Tamanho da página */
  pageSize: number;

  /** Página inicial */
  initialPage: number;

  /** Carregar mais automaticamente ao final */
  autoLoad: boolean;
}
