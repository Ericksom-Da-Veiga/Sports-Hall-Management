# Troubleshooting e Problemas Conhecidos - Sistema de Mensagens

## Problemas Comuns e Soluções

### 1. WebSocket não conecta

**Sintoma**: Indicador fica vermelho, `connected$` false

**Causas possíveis**:
- [ ] Backend não está rodando
- [ ] URL incorreta (`wsUrl`)
- [ ] Token inválido/expirado
- [ ] CORS não configurado
- [ ] SockJS não funcionando

**Solução**:
```typescript
// 1. Verificar se backend está rondando
curl http://localhost:8080/messages

// 2. Verificar token
const token = localStorage.getItem('token');
console.log('Token:', token);
jwt_decode(token); // Verificar validade

// 3. Ativar debug do STOMP
// Já está ativado no serviço - ver console para [STOMP] logs

// 4. Verificar network tab no DevTools
// Filtrar por "ws" ou "websocket"
// Ver status 101 Switching Protocols

// 5. Testar conectividade básica
const sock = new SockJS('http://localhost:8080/ws');
sock.onopen = () => console.log('Conectado');
sock.onerror = (e) => console.log('Erro:', e);
```

**Próximos passos**:
- Verificar logs do backend para erros de autenticação
- Verificar se o token tem os claims esperados
- Testar com curl ou Postman
- Ativar CORS permissivo temporariamente para debug

---

### 2. Mensagens não chegam em tempo real

**Sintoma**: Mensagem enviada por A não aparece imediatamente em B

**Causas possíveis**:
- [ ] Não inscrito ao tópico correto
- [ ] IDs de usuário diferentes em cada cliente
- [ ] Backend não faz broadcast
- [ ] Filtro de STOMP bloqueando

**Verificação**:
```typescript
// 1. Ver tópico atual no console
messagesService.currentConversationTopic

// 2. Calcular manualmente o tópico esperado
const minId = Math.min(1, 2); // 1
const maxId = Math.max(1, 2); // 2
const topic = `/topic/conversation.${minId}.${maxId}`;
console.log('Tópico esperado:', topic);

// 3. Verificar se subscrita
// Ver logs [STOMP] "Inscrito ao tópico:"

// 4. Verificar IDs de usuário
const user = AuthService.getUserFromToken();
console.log('Meu ID:', user.sub);
```

**Solução**:
- Verificar que ambos clientes usam mesmos IDs
- Verificar cálculo do tópico (IDs devem estar ORDENADOS)
- Backend deve fazer `convertAndSend` para tópico correto

---

### 3. Histórico de mensagens não carrega

**Sintoma**: Lista vazia ou "Carregando..." infinito

**Causas possíveis**:
- [ ] Endpoint não existe
- [ ] Autenticação falha (401)
- [ ] Erro SQL no backend
- [ ] ID de usuário errado

**Debug**:
```typescript
// 1. Verificar chamada HTTP no Network tab
// GET /messages/conversation/{otherUserId}

// 2. Verificar status code
// 200: OK (mas dados vazios?)
// 401: Unauthorized - verificar token
// 404: Endpoint não existe
// 500: Erro no servidor

// 3. Testar manualmente
const token = localStorage.getItem('token');
fetch('http://localhost:8080/messages/conversation/2', {
  headers: { Authorization: `Bearer ${token}` }
})
  .then(r => console.log('Status:', r.status), r.json())
  .then(d => console.log('Dados:', d));

// 4. Verificar erro no componente
console.log('errorMessage:', chatComponent.errorMessage);
```

**Solução**:
- Verificar endpoint no backend: `GET /messages/conversation/{id}`
- Verificar autenticação no backend
- Verificar se há REALMENTE dados no BD
- Testar com Postman/curl

---

### 4. Indicador de conexão fica piscando

**Sintoma**: Indicador alternando entre verde e vermelho rapidamente

**Causas possíveis**:
- [ ] Reconexão contínua (problemas de rede)
- [ ] Token expirando constantemente
- [ ] Backend rejeitando conexões
- [ ] Intervalo de reconexão muito curto

**Verificação**:
```typescript
// Ver console para [STOMP] logs
// Procurar por "CONECTADO" e "ERRO" alternados

// Ativar mais debug
messagesService.stompClient.debug = (str) => {
  console.log('[STOMP DEBUG]', str);
};

// Ver intervalo de reconexão
messagesService.stompClient.reconnectDelay; // Default: 5000ms
```

**Soluções**:
- Aumentar `reconnectDelay` para 10000ms
- Verificar estabilidade de rede
- Renovar token se próximo de expiração
- Verificar logs do backend

---

### 5. Erro: "Cannot read property 'subscribe' of null"

**Sintoma**: Erro no console ao tentar enviar mensagem

**Causa**: WebSocket não conectado quando `.subscribe()` chamado

**Solução**:
```typescript
// Verificar antes de usar
sendMessage(): void {
  if (!this.messagesService.isConnected()) {
    console.error('WebSocket não conectado');
    return this.sendMessageViaRest(); // Fallback
  }
  // Enviar via WebSocket
}

// Ou aguardar conexão
ngOnInit() {
  this.messagesService.connectWebSocket(token).then(() => {
    console.log('Seguro enviar mensagens agora');
  });
}
```

---

### 6. LocalStorage não tem token

**Sintoma**: Erro ao conectar: "Token não disponível"

**Causa**: Usuário não logado ou logout anterior

**Verificação**:
```typescript
// Verificar localStorage
console.log('Token:', localStorage.getItem('token'));
console.log('Todas as keys:', Object.keys(localStorage));

// Verificar se login persistiu
console.log('isAuthenticated:', AuthService.isAuthenticated());
```

**Solução**:
- Fazer login primeiro
- Verificar se login salva token em localStorage
- Adicionar guard na rota para forçar login

---

### 7. Mensagens chegam mas não exibem

**Sintoma**: `newMessage$` emite mas UI não atualiza

**Causa**: Angular Change Detection não detectou mudança

**Verificação**:
```typescript
// Ver se newMessage$ está sendo chamado
messagesService.newMessage$.subscribe(msg => {
  console.log('Mensagem recebida:', msg);
  console.log('Mensagens atuais:', this.messages);
});

// Forçar detecção de mudanças
constructor(private cdr: ChangeDetectorRef) {}
ngAfterViewChecked() {
  this.cdr.detectChanges();
}
```

**Solução**:
- Garantir que `addMessage()` usa `next()` corretamente
- Usar change detection strategy `OnPush` se necessário
- Verificar se array reference muda (usar spread operator `[...]`)

---

### 8. Múltiplas subscrições ao mesmo tópico

**Sintoma**: Mensagens duplicadas ou processadas 2x

**Causa**: `subscribeToConversation()` chamado sem desinscrever anterior

**Verificação**:
```typescript
// Ver número de subscriptions
messagesService.stompClient.subscriptions;

// Deve ter apenas 1 por conversa
```

**Solução**:
- Chamar `unsubscribeFromConversation()` antes de `subscribeToConversation()`
- Implementado correto no componente (chamado em `onUserChange()`)
- Verificar se cleanup do `ngOnDestroy()` funciona

---

### 9. CORS ou Preflight Error

**Sintoma**: Erro 403 ou CORS error no Network tab

**Exemplo de erro**:
```
Access to XMLHttpRequest at 'http://localhost:8080/messages' from origin 
'http://localhost:4200' has been blocked by CORS policy
```

**Solução no Backend (Spring)**:
```java
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
      .setAllowedOrigins("*")
      .withSockJS();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/messages/**")
          .allowedOrigins("*")
          .allowedMethods("GET", "POST", "PUT", "DELETE")
          .allowCredentials(false);
      }
    };
  }
}
```

---

### 10. Performance: Lentidão com muitas mensagens

**Sintoma**: UI travada com 100+ mensagens

**Causas**:
- [ ] Sem virtualização de lista
- [ ] *ngFor renderizando todos os itens
- [ ] Change detection muito agressivo

**Soluções**:

1. **Implementar CDK Virtual Scroll**:
```bash
npm install @angular/cdk
```

```typescript
import { ScrollingModule } from '@angular/cdk/scrolling';

@NgModule({
  imports: [ScrollingModule, ...]
})
export class ChatModule { }
```

```html
<cdk-virtual-scroll-viewport itemSize="50" class="messages-container">
  <div *cdkVirtualFor="let msg of messages" class="message">
    {{ msg.content }}
  </div>
</cdk-virtual-scroll-viewport>
```

2. **Paginação/Scroll Infinito**:
```typescript
loadMoreMessages() {
  this.currentPage++;
  this.messagesService.getConversation(this.selectedUserId, this.currentPage)
    .subscribe(msgs => {
      this.messages = [...msgs, ...this.messages]; // prepend
    });
}

onScroll(event: any) {
  if (event.target.scrollTop === 0 && this.currentPage < this.maxPages) {
    this.loadMoreMessages();
  }
}
```

3. **ChangeDetectionStrategy.OnPush**:
```typescript
@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ChatComponent implements OnInit {
  constructor(private cdr: ChangeDetectorRef) {}

  addMessage(msg: Message) {
    this.messages.push(msg);
    // Forçar detecção apenas quando necessário
    this.cdr.markForCheck();
  }
}
```

---

### 11. "Message not found" ao marcar como lida

**Sintoma**: PUT `/messages/{id}/read` retorna 404

**Causa**: ID de mensagem inválido ou não persisted

**Verificação**:
```typescript
// Verificar ID da mensagem
console.log('Marcando como lida:', messageId);
console.log('Tipo:', typeof messageId); // deve ser number

// Verificar na BD
// SELECT * FROM mensagens WHERE id = ?
```

**Solução**:
- Garantir que `message.id` é setado pelo backend
- Não tentar marcar sem ID
- Adicionar verificação antes:

```typescript
if (!msg.id) {
  console.warn('Mensagem sem ID, não pode marcar como lida');
  return;
}
```

---

### 12. Erro: "stompClient is not defined"

**Sintoma**: ReferenceError, método não existe

**Causa**: Falta de tipo TypeScript ou import

**Solução**:
```typescript
// No serviço, adicionar:
import { Client } from '@stomp/stompjs';

private stompClient: Client | null = null; // Type it!
```

---

## Performance Best Practices

### ✓ Implementados
- WebSocket em vez de polling
- Unsubscribe automático ao trocar usuário
- Cleanup em ngOnDestroy
- Observable pattern com Subject

### ✓ Recomendados
- CDK Virtual Scroll para 100+ mensagens
- Paginação de histórico
- Índices no BD para queries
- Compressão de payload

### ✓ Monitoramento
```typescript
// Adicionar ao serviço
private messagesSent = 0;
private messagesReceived = 0;

sendMessageWebSocket(payload) {
  this.messagesSent++;
  console.log(`[Stats] Enviados: ${this.messagesSent}, Recebidos: ${this.messagesReceived}`);
  // ...
}
```

---

## Debug Mode Avançado

### Ativar logs completos:
```typescript
// Em messagesService.ts
const DEBUG = true;

connectWebSocket(token: string): Promise<void> {
  this.stompClient = new Client({
    debug: DEBUG ? (str) => console.log('[STOMP]', str) : undefined,
    // ...
  });
}

private log(msg: string, data?: any) {
  if (DEBUG) {
    console.log(`[MSG Service] ${msg}`, data || '');
  }
}
```

### Usar DevTools STOMP extension (browser):
1. Chrome WebStore: "STOMP Inspector"
2. Permite ver subscriptions e publicar mensagens manualmente

### Network Throttling (DevTools):
1. F12 → Network
2. Throttle tipo de conexão
3. Testar comportamanto com 3G/LTE

---

## Recursos Adicionais

- [STOMP.js Docs](https://stomp-js.github.io/)
- [Spring WebSocket Docs](https://spring.io/guides/gs/messaging-stomp-websocket/)
- [Angular HttpClient](https://angular.io/guide/http)
- [RxJS Documentation](https://rxjs.dev/)

**Última atualização**: 2026-02-17  
**Version**: 1.0.0
