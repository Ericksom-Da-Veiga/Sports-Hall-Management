package com.salle.sport.repository;

import com.salle.sport.entites.Messages;
import com.salle.sport.outils.Enum.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface MessageRepository extends JpaRepository<Messages, Long> {
    @Query("""
        SELECT m FROM Messages m
        WHERE m.receiverId = :receiverId
        AND m.status = :status
    """)
    List<Messages> findByReceiverIdAndStatus(
            @Param("receiverId") Long receiverId,
            @Param("status") MessageStatus status
    );

    @Query("""
        SELECT m FROM Messages m
        WHERE (m.senderId = :user1 AND m.receiverId = :user2)
           OR (m.senderId = :user2 AND m.receiverId = :user1)
        ORDER BY m.timestamp ASC
    """)
    List<Messages> findConversation(
            @Param("user1") Long user1,
            @Param("user2") Long user2
    );
}
