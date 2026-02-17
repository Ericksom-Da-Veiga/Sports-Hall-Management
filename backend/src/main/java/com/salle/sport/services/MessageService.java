package com.salle.sport.services;

import com.salle.sport.entites.Messages;
import com.salle.sport.outils.Enum.MessageStatus;
import com.salle.sport.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.LocalDateTime;
import java.util.List;
import static com.salle.sport.outils.Enum.MessageStatus.READ;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


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

    /**
     * Helper que cria o nome do tópico comum a uma conversa entre dois IDs.
     * A ordenação garante que conversas user1/user2 e user2/user1 usem o mesmo tópico.
     */
    public String conversationTopic(Long user1, Long user2) {
        long a = Math.min(user1, user2);
        long b = Math.max(user1, user2);
        return "/topic/conversation." + a + "." + b;
    }

    /**
     * Envia e persiste uma mensagem, e a encaminha para todos os assinantes
     * do tópico de conversa apropriado.
     */
    public Messages sendAndBroadcast(Messages messages) {
        Messages saved = saveMessage(messages);
        String topic = conversationTopic(messages.getSenderId(), messages.getReceiverId());
        messagingTemplate.convertAndSend(topic, saved);
        return saved;
    }

    /**
     * Recupera a conversa entre dois utilizadores e marca como lidas
     * todas as mensagens que foram recebidas pelo primeiro utilizador.
     * Esta versão é útil quando o primeiro parâmetro é o id do utilizador
     * autenticado.
     */
    public List<Messages> getConversationAndMarkRead(Long user1, Long user2) {
        List<Messages> conv = getConversation(user1, user2);
        conv.stream()
                .filter(m -> m.getReceiverId().equals(user1) && m.getStatus() == MessageStatus.SENT)
                .forEach(m -> markAsRead(m.getId()));
        return conv;
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
