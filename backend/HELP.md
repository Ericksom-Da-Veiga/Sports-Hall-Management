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

## Chat over WebSocket

A STOMP endpoint is exposed at `/ws`. Clients should:

1. Connect using SockJS or a standard WebSocket.
2. Subscribe to conversation topics such as `/topic/conversation.{minId}.{maxId}` (ids are sorted).
3. Send messages to `/app/chat` with payload `{ "senderId":1, "receiverId":2, "content":"hello" }`.

The backend persists every message and pushes it to all subscribers of the
conversation topic. Authentication is done via the same JWT token used for
REST (include it in the `Authorization` header during the handshake).

