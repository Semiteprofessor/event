package com.event.events.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "messages",
        indexes = {
                @Index(name = "idx_message_chatroom", columnList = "chatRoom"),
                @Index(name = "idx_message_sender", columnList = "sender"),
                @Index(name = "idx_message_receiver", columnList = "receiver")
        }
)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String chatRoom;

    @NotBlank
    @Column(nullable = false)
    private String sender;

    @NotBlank
    @Column(nullable = false)
    private String receiver;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Builder.Default
    private boolean read = false;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}