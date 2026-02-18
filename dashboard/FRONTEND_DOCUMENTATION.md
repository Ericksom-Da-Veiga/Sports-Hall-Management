# Visão Geral do Frontend

Este documento descreve o frontend baseado em Angular para o painel Sports Hall Management. Ele cobre a arquitetura, a estrutura de diretórios, recursos principais e instruções para executar e estender a aplicação.

---

## 1. Informações Gerais

- **Framework**: Angular 16.2.14 (projeto gerado pelo CLI)
- **Linguagem**: TypeScript, HTML, SCSS
- **Objetivo principal**: Fornecer um painel administrativo para gerenciar assinaturas, membros, treinadores, pagamentos, esportes e configurações.


## 2. Executando a aplicação

1. Instale as dependências com `npm install`.
2. Inicie o servidor de desenvolvimento com `ng serve` ou `npm run start`.
3. Abra o navegador em `http://localhost:4200/`.
4. O servidor monitora alterações nos arquivos e recarrega automaticamente.

O `package.json` existente também contém scripts para build (`ng build`), testes unitários (`ng test`) e e2e (`ng e2e`).

## 3. Estrutura de diretórios

```
src/
  app/                 # código da aplicação
    components/        # componentes de UI reutilizáveis (cards, tabelas, formulários, navegação, etc.)
    services/          # serviços injetáveis que lidam com chamadas à API e lógica de negócio
    interceptors/      # interceptadores HTTP (por exemplo, tratamento de token)
    view/              # componentes de rota para cada página/visualização
  assets/              # JSON estáticos e outros recursos usados em desenvolvimento
```

### Pastas principais

- **components**: pequenos trechos de UI usados em várias visualizações (`dashboard-cards`, `table-users`, etc.)
- **services**: cada subpasta corresponde a um domínio (user, abonnement, payement, sport, etc.). Os serviços se comunicam com a API do backend e expõem observáveis.
- **interceptors/token.interceptor.ts**: anexa o token de autenticação às requisições HTTP.
- **view**: as páginas da aplicação, organizadas por recurso (ex.: `dashboard`, `login`, `abonnement`, `users`, etc.), cada uma com subpáginas de adicionar/editar/detalhe.


## 4. Rotas

As rotas da aplicação são definidas em `app-routing.module.ts`. Elas protegem rotas autenticadas usando um guarda de autenticação (`auth-gard` service). Rotas públicas como login são acessíveis sem autenticação.

Exemplos de rotas:
```ts
{ path: 'login', component: LoginComponent },
{ path: '', component: DashboardViewComponent, canActivate: [AuthGard] },
{ path: 'users', component: UsersViewComponent, canActivate: [AuthGard] },
// ... e outras para abonnements, payements, sports, settings, etc.
```


## 5. Gerenciamento de estado e formulários

- Os formulários utilizam formulários reativos ou template-driven do Angular, dependendo do componente. Componentes de formulário ficam em `components` ou dentro da pasta `view` do recurso (ex.: `form-users`).
- Os serviços retornam fluxos `Observable` de requisições HTTP aos quais os componentes se inscrevem. Alguns componentes usam o pipe `async` para simplificar as inscrições.


## 6. Adicionando funcionalidades

1. Gere um novo componente ou serviço usando o CLI: `ng generate component {nome}` / `ng generate service {nome}`.
2. Adicione entradas de rota em `app-routing.module.ts` e atualize os menus de navegação (`navbar`, `sidenav`).
3. Crie métodos no serviço correspondente para chamar endpoints da API do backend.
4. Utilize componentes existentes (tabelas, formulários) para manter consistência ou crie novos conforme necessário.


## 7. Estilos e temas

Os estilos são definidos em arquivos SCSS localizados ao lado de seus componentes. Estilos globais podem ser adicionados em `styles.scss`. O projeto utiliza Angular Material para componentes de UI (verificar dependências no `package.json`).


## 8. Testes

- Testes unitários ficam ao lado dos componentes (`*.spec.ts`) e são executados com Karma/Jasmine usando `ng test`.
- Testes de ponta a ponta podem ser adicionados com Protractor ou Cypress (não incluídos por padrão).


## 9. Observações e referências

- Detalhes de integração Websocket estão em `WEBSOCKET_INTEGRATION_GUIDE.md`.
- Consulte `IMPLEMENTATION_SUMMARY.md` para arquitetura geral do sistema incluindo backend.
- `TROUBLESHOOTING.md` e `TESTING_CHECKLIST.md` contêm procedimentos úteis.

---

Esta documentação serve como ponto de partida. Para informações mais detalhadas, explore módulos individuais em `src/app` e guias Markdown associados.