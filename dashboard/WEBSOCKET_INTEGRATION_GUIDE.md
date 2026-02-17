# Guia de Integração WebSocket - Sistema de Mensagens

## Visão Geral

O sistema de mensagens foi completamente integrado com suporte a WebSocket/STOMP para comunicação em tempo real, mantendo fallback para HTTP quando necessário.

## Arquitetura

### Backend (Node.js/Spring)
- **Endpoint WebSocket**: `/ws` (com suporte SockJS)
- **Prefixo de aplicação**: `/app`
- **Tópico de conversa**: `/topic/conversation.{minId}.{maxId}` (IDs ordenados)
- **Mapping STOMP**: `@MessageMapping("/chat")`

### Frontend (Angular)

#### Dependências Instaladas
```json
{
  "@stomp/stompjs": "^7.x",
  "sockjs-client": "^1.x"
}
```

#### Serviço Principal: `MessagesService`

**Métodos de Conexão:**
- `connectWebSocket(token)` - Conecta ao servidor com autenticação JWT
- `disconnectWebSocket()` - Desconecta e limpa recursos
- `isConnected()` - Verifica status de conexão

**Métodos de REST:**
- `getConversation(otherUserId)` - Obtém histórico com o utilizador logado
- `getConversationGeneric(user1, user2)` - Obtém histórico genérico
- `getUnreadMessages(receiverId)` - Obtém mensagens não lidas
- `markMessageAsRead(messageId)` - Marca como lida
- `sendMessageRest(message)` - Envia via HTTP (fallback)

**Métodos de WebSocket:**
- `subscribeToConversation(user1, user2)` - Subscreve a um tópico
- `unsubscribeFromConversation()` - Desinscreve
- `sendMessageWebSocket(payload)` - Envia via WebSocket

**Observables Públicos:**
- `messages$` - Stream de mensagens atuais
- `connected$` - Status de conexão
- `newMessage$` - Nova mensagem recebida

#### Componente: `ChatComponent`

**Fluxo de Inicialização:**
1. Carrega lista de utilizadores
2. Conecta ao WebSocket (async)
3. Subscreve a novas mensagens via WebSocket
4. Monitora status de conexão
5. Carrega histórico de conversa
6. Inscreve-se ao tópico de conversa

**Envio de Mensagens:**
- WebSocket (preferido): Enviado para `/app/chat`
- HTTP (fallback): Quando WebSocket está indisponível
- Automático: Alternância automática baseada em conectividade

## Interface Message

```typescript
interface Message {
  id?: number;
  senderId: number;
  receiverId: number;
  content: string;
  timestamp?: Date | string;
  senderName?: string;
  status?: 'SENT' | 'DELIVERED' | 'READ';
  read?: boolean;
}
```

## Fluxo de Mensagens

### Envio (Frontend → Backend)

1. Utilizador digita e envia mensagem
2. Frontend valida conteúdo
3. **Se WebSocket conectado:**
   - Envia payload para `/app/chat`
   - Backend processa em `MessageController`
   - Salva em BD via `MessageService.saveMessage()`
   - Broadcast para `/topic/conversation.{minId}.{maxId}`
4. **Se WebSocket indisponível:**
   - Utiliza fallback HTTP POST `/messages`
   - Sincronização manual após resposta

### Recebimento (Backend → Frontend)

1. Backend envia para tópico de conversa
2. Frontend subscrito recebe via WebSocket
3. Mensagem parseada e adicionada ao stream `newMessage$`
4. Componente atualiza vista automaticamente
5. Storage local atualizado

### Marcação como Lida

1. Carregamento de conversa: marca todas as recebidas como lidas
2. PUT `/messages/{messageId}/read`
3. Status atualizado no backend

## Instalação e Configuração

### 1. Instalar Dependências

```bash
npm install @stomp/stompjs sockjs-client
npm install --save-dev @types/sockjs-client
```

### 2. Variáveis de Ambiente

Verificar configuração no `MessagesService`:
- `apiUrl`: `http://localhost:8080/messages`
- `wsUrl`: `http://localhost:8080/ws`

Para produção, usar URLs relativas ou variáveis de ambiente:

```typescript
private apiUrl = environment.apiUrl || 'http://localhost:8080/messages';
private wsUrl = environment.wsUrl || 'http://localhost:8080/ws';
```

### 3. Módulo Angular

Certificar que `HttpClientModule` e `FormsModule` estão importados:

```typescript
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [HttpClientModule, FormsModule, ...]
})
export class AppModule { }
```

### 4. Autenticação

O JWT token é enviado automaticamente no header durante handshake:
```typescript
connectHeaders: {
  Authorization: `Bearer ${token}`
}
```

## Estados e Tratamento de Erros

### Estados de Conexão

- **Conectado**: Verde, pulsante
  - Mensagens enviadas via WebSocket
  - Recebimento em tempo real
  
- **Desconectado**: Vermelho
  - Fallback para HTTP
  - Reconexão automática a cada 5s

### Tratamento de Falhas

1. **Erro na conexão WebSocket**:
   - Mensagem de erro exibida
   - Fallback para HTTP automático
   - Reconexão periódica

2. **Erro ao enviar mensagem**:
   - Tenta WebSocket
   - Se falhar, tenta HTTP
   - Exibe erro se ambos falharem

3. **Erro ao marcar como lida**:
   - Log no console
   - Não bloqueia UI
   - Retry silencioso

## Otimizações Implementadas

### Performance
- **Lazy loading de mensagens**: Histórico carregado sob demanda
- **Subscrição seletiva**: Apenas tópico relevante
- **Limpeza automática**: Desinscrição ao trocar de utilizador
- **Scroll automático**: Para últimas mensagens

### Escalabilidade
- **Tópicos organizados**: Por par de utilizadores
- **Identificadores únicos**: Evita duplicatas
- **Fallback automático**: Nunca perde mensagens

### Segurança
- **JWT em header**: Autenticação em WebSocket
- **Validação de IDs**: Apenas utilizadores autorizados
- **HTTPS/WSS**: Configurável para produção

## Exemplos de Uso

### Usar o ChatComponent

```html
<app-chat></app-chat>
```

### Usar MessagesService Diretamente

```typescript
constructor(private messagesService: MessagesService) {}

ngOnInit() {
  // Conectar
  const token = localStorage.getItem('token');
  this.messagesService.connectWebSocket(token).then(() => {
    console.log('Conectado');
  });

  // Subscrever a mensagens
  this.messagesService.newMessage$.subscribe(msg => {
    console.log('Nova mensagem:', msg);
  });

  // Obter histórico
  this.messagesService.getConversation(2).subscribe(msgs => {
    console.log('Histórico:', msgs);
  });

  // Enviar via WebSocket
  this.messagesService.sendMessageWebSocket({
    senderId: 1,
    receiverId: 2,
    content: 'Olá!'
  });

  // Marcar como lida
  this.messagesService.markMessageAsRead(5).subscribe();
}

ngOnDestroy() {
  this.messagesService.disconnectWebSocket();
}
```

## Debugging

### Logs STOMP

O serviço printa logs automáticos em `[STOMP]`:
- Ativação de conexão
- Subscrições
- Recebimento de mensagens
- Erros

### Monitorar no Console

```typescript
// Ver status de conexão
messagesService.isConnected() // true/false

// Ver todas as mensagens atuais
messagesService.messagesSubject.value

// Subscrever para debug
messagesService.connected$.subscribe(status => {
  console.log('WebSocket:', status);
});
```

## Próximos Passos

1. **Testar integração completa**:
   - Iniciar backend em `localhost:8080`
   - Verificar conexão WebSocket
   - Testar envio/recebimento

2. **Configurar para produção**:
   - Usar URLs HTTPS/WSS
   - Implementar variáveis de ambiente
   - Testes de carga

3. **Melhorias futuras**:
   - Notificações de digitação
   - Confirmação de entrega
   - Histórico com paginação
   - Busca em conversas
   - Suporte a anexos

## Troubleshooting

### WebSocket não conecta
- Verificar se backend está rodando em `localhost:8080`
- Verificar token JWT válido em `localStorage`
- Verificar CORS no backend

### Mensagens não aparecem
- Verificar se WebSocket está conectado (verde no header)
- Ver logs no console do browser
- Verificar IDs de utilizador

### Lentidão
- Limpar histórico (recarga página)
- Verificar conexão de rede
- Monitorar recursos do browser

## Referências

- [STOMP.js Documentation](https://stomp-js.github.io/)
- [SockJS Documentation](https://github.com/sockjs/sockjs-client)
- [Angular HttpClient](https://angular.io/guide/http)
- [RxJS](https://rxjs.dev/)
