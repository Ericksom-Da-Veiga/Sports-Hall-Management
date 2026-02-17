package com.salle.sport.controllers;

import com.salle.sport.dto.message.DTO_post_message;
import com.salle.sport.entites.Messages;
import com.salle.sport.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // 🔹 Enviar nova mensagem
    @PostMapping
    public Messages sendMessage(@RequestBody DTO_post_message message) {
        Messages mensagem = new Messages(message.senderId(), message.receiverId(), message.message());
        return messageService.saveMessage(mensagem);
    }

    // 🔹 Buscar conversa entre dois utilizadores
    @GetMapping("/conversation")
    public List<Messages> getConversation(@RequestParam Long user1_id,@RequestParam Long user2_id) {
        return messageService.getConversation(user1_id, user2_id);
    }

    // 🔹 Buscar mensagens não lidas de um utilizador
    @GetMapping("/unread/{receiverId}")
    public List<Messages> getUnreadMessages(@PathVariable Long receiverId) {
        return messageService.getUnreadMessages(receiverId);
    }

    // 🔹 Marcar mensagem como lida
    @PutMapping("/{messageId}/read")
    public Messages markAsRead(@PathVariable Long messageId) {
        return messageService.markAsRead(messageId);
    }
}
