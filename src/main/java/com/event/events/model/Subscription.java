package com.event.events.model;

import com.event.events.enums.SubscriptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        name = "subscriptions",
        indexes = {
                @Index(
                        name = "idx_subscription_user",
                        columnList = "userId"
                ),
                @Index(
                        name = "idx_subscription_plan",
                        columnList = "planId"
                ),
                @Index(
                        name = "idx_subscription_status",
                        columnList = "status"
                )
        }
)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String userId;

    @NotBlank
    @Column(nullable = false)
    private String planId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @NotNull
    @Column(nullable = false)
    private Instant startDate;

    @NotNull
    @Column(nullable = false)
    private Instant endDate;

    @NotBlank
    @Column(nullable = false)
    private String paymentProvider; // stripe / paystack

    @NotBlank
    @Column(nullable = false)
    private String paymentReference;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}