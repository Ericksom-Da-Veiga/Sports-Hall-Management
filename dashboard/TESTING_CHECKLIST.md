# Checklist de Implementação e Testes - Sistema de Mensagens WebSocket

## Fase 1: Configuração Inicial ✓

- [ ] **Dependências instaladas**
  - [ ] `@stomp/stompjs` versão 7.x ou superior
  - [ ] `sockjs-client` versão 1.x ou superior
  - [ ] Tipos TypeScript instalados
  - Comando: `npm install @stomp/stompjs sockjs-client`

- [ ] **Backend rodando**
  - [ ] Servidor rodando em `localhost:8080`
  - [ ] Endpoint `/ws` acessível
  - [ ] REST endpoints `/messages/*` respondendo
  - [ ] JWT authentication configurada

- [ ] **Módulos Angular importados**
  - [ ] `HttpClientModule` em `app.module.ts`
  - [ ] `FormsModule` em `app.module.ts`
  - [ ] `CommonModule` em componentes com `*ngIf`, `*ngFor`

## Fase 2: Serviço MessagesService ✓

- [ ] **Métodos de Conexão**
  - [ ] `connectWebSocket()` funciona com token JWT
  - [ ] `disconnectWebSocket()` limpa recursos
  - [ ] Reconexão automática após desconexão
  - [ ] Status de conexão visível no observable `connected$`

- [ ] **Métodos REST**
  - [ ] `getConversation(otherUserId)` retorna histórico
  - [ ] `getUnreadMessages(receiverId)` retorna não-lidas
  - [ ] `markMessageAsRead(messageId)` marca como lida
  - [ ] `sendMessageRest(message)` envia via HTTP

- [ ] **Métodos WebSocket**
  - [ ] `subscribeToConversation(u1, u2)` subscreve corretamente
  - [ ] Tópico gerado com IDs ordenados: `/topic/conversation.{min}.{max}`
  - [ ] `sendMessageWebSocket()` envia para `/app/chat`
  - [ ] `newMessage$` emite quando mensagem recebida

- [ ] **Observables Públicos**
  - [ ] `messages$` atualiza ao carregar/adicionar
  - [ ] `connected$` reflete estado real
  - [ ] `newMessage$` emite apenas novas

## Fase 3: Componente ChatComponent ✓

- [ ] **Inicialização**
  - [ ] Carrega lista de utilizadores
  - [ ] Conecta ao WebSocket durante `ngOnInit`
  - [ ] Obtém ID do utilizador do token JWT
  - [ ] Inscreve-se a mudanças de status

- [ ] **Seleção de Utilizador**
  - [ ] Lista exibe corretamente
  - [ ] Mudar utilizador desinscreve do tópico anterior
  - [ ] Carrega histórico ao mudar
  - [ ] Marca como lida automaticamente

- [ ] **Exibição de Mensagens**
  - [ ] Mensagens próprias alinhadas à direita
  - [ ] Mensagens alheias alinhadas à esquerda
  - [ ] Timestamps formatados corretamente
  - [ ] Scroll automático para última mensagem

- [ ] **Envio de Mensagens**
  - [ ] Envia via WebSocket se conectado
  - [ ] Fallback para HTTP se desconectado
  - [ ] Campo de input limpo após envio
  - [ ] Erro exibido se ambos falham

- [ ] **Status Visual**
  - [ ] Indicador de conexão mostra corretamente
  - [ ] Verde quando conectado
  - [ ] Vermelho quando desconectado
  - [ ] Pulsação animada

## Fase 4: Testes Funcionais

### Teste de Conexão
- [ ] **Conectar com usuário logado**
  - [ ] Token válido lido do localStorage
  - [ ] Handshake WebSocket bem-sucedido
  - [ ] Console mostra "[STOMP] Conectado ao WebSocket"
  - [ ] Indicador muda para verde

- [ ] **Desconectar**
  - [ ] `ngOnDestroy` chama `disconnectWebSocket()`
  - [ ] STOMP client desativado
  - [ ] Indicador muda para vermelho
  - [ ] Reconexão automática após 5s (testar novo ngOnInit)

- [ ] **Falha de autenticação**
  - [ ] Token inválido rejeitado
  - [ ] Mensagem de erro exibida
  - [ ] Fallback para HTTP funciona

### Teste de Mensagens
- [ ] **Envio via WebSocket**
  - [ ] Abrir 2 browsers/abas com usuários diferentes
  - [ ] Usuário A envia mensagem para B
  - [ ] Usuário B recebe em tempo real (<1s)
  - [ ] Mensagem exibe corretamente

- [ ] **Envio via HTTP (fallback)**
  - [ ] Desabilitar WebSocket (comment `connectWebSocket`)
  - [ ] Enviar mensagem
  - [ ] Verifica endpoint POST `/messages`
  - [ ] Mensagem salva no backend

- [ ] **Histórico de Conversa**
  - [ ] Mudar de usuário mostra histórico
  - [ ] Mensagens antigas aparecem
  - [ ] Timestamps corretos
  - [ ] Ordem cronológica mantida

- [ ] **Marcar como Lida**
  - [ ] Abrir conversa marca como lidas
  - [ ] PUT `/messages/{id}/read` chamado
  - [ ] Status atualizado no backend

- [ ] **Novas Mensagens em Tempo Real**
  - [ ] Subscrição ao tópico funciona
  - [ ] Novas mensagens aparecem sem refresh
  - [ ] Scroll automático para última
  - [ ] Múltiplas conversa paralelas

### Teste de UI
- [ ] **Responsividade**
  - [ ] Desktop (1920x1080): layout normal
  - [ ] Tablet (768x1024): design mobile
  - [ ] Mobile (375x667): texto legível, buttons acessíveis

- [ ] **Acessibilidade**
  - [ ] Teclado: Tab através de elementos
  - [ ] Enter em input envia mensagem
  - [ ] Cores com contraste suficiente
  - [ ] Labels associados a inputs

- [ ] **Performance**
  - [ ] 100+ mensagens carregam rápido (<2s)
  - [ ] Scroll fluido
  - [ ] Sem memory leaks após múltiplas conversa

### Teste de Erros
- [ ] **Conexão Perdida**
  - [ ] Desligar internet
  - [ ] Botão send fica desabilitado
  - [ ] Conexão retorna quando online
  - [ ] Mensagens pendentes enviadas

- [ ] **Backend Indisponível**
  - [ ] Backend offline
  - [ ] Erro exibido corretamente
  - [ ] Tentativa de reconexão
  - [ ] Usuário pode tentar novamente

- [ ] **Validação**
  - [ ] Mensagem vazia não envia
  - [ ] Campo desabilitado se sem usuário selecionado
  - [ ] Limite de caracteres respeitado (se houver)

## Fase 5: Testes de Integração

- [ ] **Com AuthService**
  - [ ] Token lido corretamente do localStorage
  - [ ] Logout desconecta WebSocket
  - [ ] Novo login reconecta

- [ ] **Com UserService**
  - [ ] Lista de usuários carrega
  - [ ] IDs de usuários coincidem
  - [ ] Filtro de usuário (excluir self) funciona

- [ ] **Com ToastrService (notificações)**
  - [ ] Notificação ao receber mensagem
  - [ ] Som (se implementado)
  - [ ] Badge de não-lidas (se implementado)

## Fase 6: Testes de Carga

- [ ] **Múltiplas Conversa**
  - [ ] 10+ conversas abertas simultaneamente
  - [ ] Troca rápida entre elas
  - [ ] Sem lag perceptível

- [ ] **Histórico Grande**
  - [ ] Conversa com 1000+ mensagens
  - [ ] Carrega e exibe (paginação ou scroll infinito)
  - [ ] Performance aceitável

- [ ] **Muitos Usuários Online**
  - [ ] Aplicação com 100+ utilizadores
  - [ ] Sem degradação de performance
  - [ ] Websocket mantém conexão estável

## Fase 7: Testes de Segurança

- [ ] **Verificação de Token**
  - [ ] Token expirado não conecta WebSocket
  - [ ] Mensagens sem auth rejeitadas
  - [ ] Apenas conversas autorizadas carregam

- [ ] **Validação de IDs**
  - [ ] Não pode enviar para ID inválido
  - [ ] Não pode ver conversa de outro usuário
  - [ ] IDs verificados no backend

- [ ] **HTTPS/WSS (em produção)**
  - [ ] URL usa https://
  - [ ] WebSocket usa wss://
  - [ ] Certificate válido

## Fase 8: Browser Compatibility

- [ ] **Chrome/Chromium (latest)**
  - [ ] Tudo funciona
  - [ ] DevTools sem erros

- [ ] **Firefox (latest)**
  - [ ] WebSocket funciona
  - [ ] SockJS fallback (se necessário)
  - [ ] DevTools sem erros

- [ ] **Safari (latest)**
  - [ ] Compatibilidade verificada
  - [ ] LocalStorage funciona

- [ ] **Edge (latest)**
  - [ ] Tudo funciona como Chrome

## Fase 9: Documentação e Manutenção

- [ ] **Código documentado**
  - [ ] Métodos têm JSDoc
  - [ ] Tipos claramente definidos
  - [ ] Comentários explicam lógica complexa

- [ ] **Exemplos fornecidos**
  - [ ] Como conectar
  - [ ] Como usar em outros componentes
  - [ ] Como debug

- [ ] **Logs úteis**
  - [ ] Console logs não poluem output
  - [ ] Erros claramente identificados
  - [ ] Timestamps em logs

## Fase 10: Deploy e Produção

- [ ] **Build produção**
  - [ ] `ng build --configuration production` sem erros
  - [ ] Bundle size razoável
  - [ ] Minificação funcionando

- [ ] **Environment config**
  - [ ] URLs baseadas em environment
  - [ ] Sem hardcodes localhost
  - [ ] WSS em produção

- [ ] **Monitoramento**
  - [ ] Logs de erro enviados para Analytics
  - [ ] Performance monitorada
  - [ ] User feedback collection (opcional)

## Pontuação Final

- [ ] Checklist 100% concluído
- [ ] Todos os testes passaram
- [ ] Documentação completa
- [ ] Pronto para produção

---

## Notas Importantes

### Endpoints esperados no backend:

```
POST   /messages
  Body: { senderId, receiverId, content }
  Response: Message{ id, ... }

GET    /messages/conversation/{otherUserId}
  Response: Message[]

GET    /messages/conversation?user1_id=X&user2_id=Y
  Response: Message[]

GET    /messages/unread/{receiverId}
  Response: Message[]

PUT    /messages/{messageId}/read
  Response: { success: true }

WS     /ws
  Header: Authorization: Bearer <token>

STOMP  @MessageMapping("/chat")
  Payload: { senderId, receiverId, content }
  Broadcast: /topic/conversation.{min}.{max}
```

### Debug Console Commands:

```typescript
// Ver status WebSocket
ng.probe(document.querySelector('app-chat')).componentInstance.messagesService.isConnected()

// Ver mensagens atuais
ng.probe(document.querySelector('app-chat')).componentInstance.messagesService.messagesSubject.value

// Ver status de conexão
ng.probe(document.querySelector('app-chat')).componentInstance.isConnected

// Enviar teste
ng.probe(document.querySelector('app-chat')).componentInstance.messagesService.sendMessageWebSocket({
  senderId: 1,
  receiverId: 2,
  content: "Teste"
})
```

---

**Data de Início**: _____  
**Data de Conclusão**: _____  
**Status Final**: ⬜ Não Iniciado | 🟨 Em Progresso | 🟩 Concluído | 🟥 Falhou  
**Assinatura**: _____________________
