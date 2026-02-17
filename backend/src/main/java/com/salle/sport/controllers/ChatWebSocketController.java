package com.salle.sport.controllers;

import com.salle.sport.dto.message.DTO_post_message;
import com.salle.sport.entites.Messages;
import com.salle.sport.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private MessageService messageService;

    /**
     * Recebe mensagens enviadas pelos clientes via STOMP (/app/chat) e
     * repassa para o tópico de conversa correspondente.
     */
    @MessageMapping("/chat")
    public void handleChat(DTO_post_message msg) {
        Messages m = new Messages(msg.senderId(), msg.receiverId(), msg.content());
        messageService.sendAndBroadcast(m);
    }
}
