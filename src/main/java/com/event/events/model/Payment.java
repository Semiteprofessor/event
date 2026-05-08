package com.event.events.model;

import com.event.events.enums.PaymentStatus;
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
        name = "payments",
        indexes = {
                @Index(
                        name = "idx_payment_user_status",
                        columnList = "userId, status"
                ),
                @Index(
                        name = "idx_payment_reference",
                        columnList = "reference"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_payment_reference",
                        columnNames = {"reference"}
                )
        }
)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String userId;

    @NotBlank
    @Column(nullable = false)
    private String planId;

    @Column(nullable = false)
    private double amount;

    @NotBlank
    @Column(nullable = false)
    private String provider; // stripe / paystack

    @NotBlank
    @Column(nullable = false, unique = true)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}