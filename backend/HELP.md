# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.3.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.3.0/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.3.0/reference/htmlsingle/index.html#web)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

## Documentação do Sistema (Backend)

Este projeto é a parte backend de um sistema de gestão de instalações desportivas.
A aplicação é desenvolvida em **Java 17** usando o framework **Spring Boot** e segue
padrões de arquitetura de camadas (controller, service, repository).

### Principais funcionalidades

* Gestão de usuários (autenticação JWT, perfis de acesso).
* Controle de assinaturas e pagamentos.
* Agendamento de aulas e serviços de treino.
* Comunicação em tempo real via WebSocket com STOMP (chat entre usuários).
* Dashboard para estatísticas de uso e performance.

### Tecnologias e dependências

* Spring Boot Web MVC para APIs REST.
* Spring Data JPA com Hibernate para acesso a banco de dados relacional.
* Spring Security com JWT para autenticação e autorização.
* WebSocket/STOMP para funcionalidades de chat.
* Banco de dados configurado via `application.properties` (MySQL, H2, etc.).

### Estrutura de pacotes

O código fonte está organizado nos seguintes pacotes principais:

```
com.salle.sport
  ├─ controllers   // endpoints REST e WebSocket
  ├─ services      // lógica de negócio
  ├─ repository    // interfaces JPA para persistência
  ├─ dto           // objetos de transferência de dados
  ├─ entites       // entidades JPA
  ├─ infra         // configurações e componentes infraestruturais
  └─ outils        // utilitários diversos
```

### Como iniciar o backend

1. Configure o banco de dados em `src/main/resources/application.properties`.
2. Execute `./mvnw spring-boot:run` para iniciar a aplicação.
3. As APIs estarão disponíveis em `http://localhost:8080/api/...`.

### Testes

Teste unitários e de integração estão localizados em
`src/test/java`. Use `mvn test` para executar todos os testes.

### Chat over WebSocket

A STOMP endpoint is exposed at `/ws`. Clients should:

1. Connect using SockJS or a standard WebSocket.
2. Subscribe to conversation topics such as `/topic/conversation.{minId}.{maxId}` (ids are sorted).
3. Send messages to `/app/chat` with payload `{ "senderId":1, "receiverId":2, "content":"hello" }`.

The backend persists every message and pushes it to all subscribers of the
conversation topic. Authentication is done via the same JWT token used for
REST (include it in the `Authorization` header during the handshake).

