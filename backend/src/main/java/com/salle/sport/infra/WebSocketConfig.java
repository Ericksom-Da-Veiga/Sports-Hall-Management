package com.salle.sport.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ponto de entrada para o cliente abrir o socket
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // mensagens enviadas por clientes começam com /app
        registry.setApplicationDestinationPrefixes("/app");
        // broker simples que envia para tópicos/filas
        registry.enableSimpleBroker("/topic", "/queue");
        // prefixo para mensagens "user-to-user" (destino individual)
        registry.setUserDestinationPrefix("/user");
    }
}
