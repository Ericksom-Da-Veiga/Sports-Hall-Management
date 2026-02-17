package com.salle.sport.services;

import com.salle.sport.entites.Messages;
import com.salle.sport.outils.Enum.MessageStatus;
import com.salle.sport.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import static com.salle.sport.outils.Enum.MessageStatus.READ;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    //Enviar uma nova menssagem
    public Messages saveMessage(Messages messages) {
        messages.setTimestamp(LocalDateTime.now());
        messages.setStatus(MessageStatus.SENT);
        return messageRepository.save(messages);
    }

    //recuperar todas as menssagens para um determinado user
    public List<Messages> getConversation(Long user1, Long user2) {
        return messageRepository.findConversation(user1, user2);
    }

    // Recuperar menssagens nao lidas
    public List<Messages> getUnreadMessages(Long receiverId) {
        return messageRepository
                .findByReceiverIdAndStatus(receiverId, MessageStatus.SENT);
    }

    // Modificar os status da menssagem
    public Messages markAsRead(Long messageId) {
        Messages messages = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (messages.getStatus() == READ) {
            return messages;
        }
        messages.setStatus(READ);
        return messageRepository.save(messages);
    }

}
