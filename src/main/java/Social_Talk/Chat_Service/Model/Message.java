package Social_Talk.Chat_Service.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "message")
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int senderId;
    private int receiverId;

    @Lob
    private String content;

    @Column(name = "sent_message_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP  DEFAULT CURRENT_TIME")
    private LocalDateTime sentMessageAt;

    public Message(int senderId, int receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }
}

