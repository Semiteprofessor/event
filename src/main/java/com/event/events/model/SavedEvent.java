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
        name = "saved_events",
        indexes = {
                @Index(
                        name = "idx_saved_event_guest",
                        columnList = "guest"
                ),
                @Index(
                        name = "idx_saved_event_event",
                        columnList = "event"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_saved_event_guest_event",
                        columnNames = {"guest", "event"}
                )
        }
)
public class SavedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String guest;

    @NotBlank
    @Column(nullable = false)
    private String event;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}