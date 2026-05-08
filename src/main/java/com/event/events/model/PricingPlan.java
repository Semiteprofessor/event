package com.event.events.model;

import com.event.events.enums.BillingCycle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "pricing_plans",
        indexes = {
                @Index(
                        name = "idx_pricing_plan_slug",
                        columnList = "slug",
                        unique = true
                ),
                @Index(
                        name = "idx_pricing_plan_billing_cycle",
                        columnList = "billingCycle"
                )
        }
)
public class PricingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String plan;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String slug;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    private String tag;

    @NotEmpty
    @ElementCollection
    @CollectionTable(
            name = "pricing_plan_features",
            joinColumns = @JoinColumn(name = "pricing_plan_id")
    )
    @Column(name = "feature", nullable = false)
    private List<String> features;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingCycle billingCycle;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}