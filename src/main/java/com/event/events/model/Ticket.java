package com.event.events.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "tickets",
        indexes = {
                @Index(name = "idx_ticket_user_email", columnList = "userEmail"),
                @Index(name = "idx_ticket_event", columnList = "event"),
                @Index(name = "idx_ticket_booking", columnList = "booking"),
                @Index(name = "idx_ticket_qr_slug", columnList = "qrSlug", unique = true)
        }
)
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userEmail;

    @NotBlank
    private String event;

    @NotBlank
    private String booking;

    private Instant issueDate;

    @Column(unique = true)
    private String qrSlug;

    private String pdfUrl;

    private String qrUrl;

    @NotBlank
    private String ticketType;

    @NotNull
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Builder.Default
    private boolean checkedIn = false;

    private Instant checkedInAt;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}