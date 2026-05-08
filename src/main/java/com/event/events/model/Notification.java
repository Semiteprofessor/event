package com.event.events.model;

import com.event.events.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_notification_user", columnList = "userId"),
                @Index(name = "idx_notification_read", columnList = "read"),
                @Index(name = "idx_notification_type", columnList = "type")
        }
)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private NotificationType type = NotificationType.SYSTEM;

    @Builder.Default
    private boolean read = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}