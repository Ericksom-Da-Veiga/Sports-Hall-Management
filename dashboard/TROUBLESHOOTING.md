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

### 6. Erro 500 ao enviar mensagem com REST

**Sintoma relacionado**: histórico não aparece e/ou a lista de utilizadores omite o admin,
as mensagens são gravadas mas o destinatário não as vê.

**Diagnóstico**: o componente inicializava `myUserId` com o valor `1` por omissão. quando
um token JWT só continha um endereço de e‑mail (por exemplo no `sub`), a conversão para
número falhava e o valor do administrador era mantido. O filtro na lista de utilizadores
excluía indevidamente o admin, e todas as requisições de conversa utilizavam `senderId`
= 1 em vez do ID real do remetente. Consequentemente o histórico devolvido não correspondia
ao par de identificadores correto e os tópicos WebSocket não coincidiam, portanto o
recetor não via as novas mensagens.

**Solução**: remover o valor por omissão e calcular `myUserId` só após `GET /user` ter
retornado os utilizadores disponíveis. A ordem de verificação é:

1. extrair um número directamente de qualquer claim do token (`id`, `sub`, etc) e convertê‑lo
   com `Number()`;
2. se não houver número, extrair o e‑mail do token (claims comuns: `mail`, `email`,
   `sub`, `username`, `preferred_username`) e procurar o utilizador correspondente no
   array obtido a partir da API;
3. se ainda não conseguir determinar o id, deixar‑o `null` e não filtrar a lista (aparece
   o admin) e gerar um aviso no console. o chat passa a mostrar um erro caso se tente
   carregar conversa sem ID conhecido.

Esse comportamento já está implementado no `ChatComponent.loadUsers()` e
`loadConversation()`, portanto o problema do utilizador desaparece e a lista de
utilizadores não exclui o admin quando se está logado com outro user.

```ts
// exemplo resumido do novo código
myUserId: number | null = null;

loadUsers() {
  const tokenUser: any = this.authService.getUserFromToken();
  // ...calcular tokenNumericId e tokenEmail seguindo os passos acima...

  if (tokenNumericId != null) {
    this.myUserId = tokenNumericId;
  } else if (tokenEmail) {
    const me = allUsers.find(u => u.mail === tokenEmail);
    if (me) {
      this.myUserId = me.id;
      this.myUserName = `${me.nom} ${me.prenom}`;
    }
  }

  if (this.myUserId == null) {
    console.warn('ID do utilizador desconhecido; lista não será filtrada');
  }

  this.users = this.myUserId != null ? allUsers.filter(u => u.id !== this.myUserId) : allUsers;
}
```

---


---

### 7. LocalStorage não tem token

**Sintoma**: `Http failure response for http://localhost:8080/messages: 500 OK` com corpo contendo
```
JSON parse error: Cannot deserialize value of type `java.lang.Long` from String "admin@admin.com"
```

**Causa**: o frontend estava passando um identificador de utilizador (`senderId`/`receiverId`) como string
(provavelmente extraído de `sub` do token JWT que contém o email) em vez de um número. O backend
espera um `Long` e falha ao desserializar.

**Solução**: garantir que `myUserId` e `selectedUserId` sejam números antes de montar o payload;
converter explicitamente valores vindos do token.

```ts
const rawId = user.id ?? user.sub;
this.myUserId = Number(rawId) || 1; // tratar NaN
```

ou transformar na hora de enviar via REST:
```ts
this.messagesService.sendMessageRest({
  senderId: Number(this.myUserId),
  receiverId: Number(this.selectedUserId),
  ...
});
```

---

### 7. LocalStorage não tem token

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
