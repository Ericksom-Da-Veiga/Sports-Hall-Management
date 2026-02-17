package com.salle.sport.controllers;

import com.salle.sport.dto.message.DTO_post_message;
import com.salle.sport.entites.Messages;
import com.salle.sport.services.MessageService;
import jakarta.validation.Valid;
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

import com.salle.sport.entites.Users;
import org.springframework.security.core.context.SecurityContextHolder;
import com.salle.sport.outils.Enum.MessageStatus;

import java.util.List;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // 🔹 Enviar nova mensagem
    @PostMapping
    public Messages sendMessage(@RequestBody @Valid DTO_post_message message) {
        Messages mensagem = new Messages(message.senderId(), message.receiverId(), message.content());
        return messageService.saveMessage(mensagem);
    }

    // 🔹 Buscar conversa entre dois utilizadores (parametros explicitos)
    @GetMapping("/conversation")
    public List<Messages> getConversation(@RequestParam Long user1_id,@RequestParam Long user2_id) {
        return messageService.getConversation(user1_id, user2_id);
    }

    // 🔹 Buscar conversa entre o utilizador autenticado e outro utilizador selecionado
    // O id do usuário autenticado é obtido a partir do contexto de segurança.
    @GetMapping("/conversation/{otherUserId}")
    public List<Messages> getConversationWith(@PathVariable Long otherUserId) {
        // recuperar user logado do SecurityContext
        Users logged = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loggedId = logged.getId();

        // utiliza método especializado que já marca mensagens lidas
        return messageService.getConversationAndMarkRead(loggedId, otherUserId);
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
