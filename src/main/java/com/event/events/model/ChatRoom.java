package com.event.events.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "chat_rooms",
        indexes = {
                @Index(name = "idx_chatroom_last_sender", columnList = "lastMessageSender")
        }
)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ElementCollection
    @CollectionTable(
            name = "chat_room_participants",
            joinColumns = @JoinColumn(name = "chat_room_id")
    )
    @Column(name = "participant")
    @Builder.Default
    private List<String> participants = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String lastMessage;

    private String lastMessageSender;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}