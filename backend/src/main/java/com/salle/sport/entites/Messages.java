package com.salle.sport.entites;

import com.salle.sport.outils.Enum.MessageStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.salle.sport.outils.Enum.MessageStatus.SENT;

@Entity
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    private String content;

    private LocalDateTime timestamp;

    private MessageStatus status = SENT; //(READ / SENT);

    public Messages(Long senderId, Long receiverId, String content){
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        timestamp = LocalDateTime.now();
    }
}
