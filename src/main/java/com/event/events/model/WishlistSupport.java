package com.event.events.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
        name = "wishlist_support",
        indexes = {
                @Index(
                        name = "idx_wishlist_support_wishlist",
                        columnList = "wishlist"
                ),
                @Index(
                        name = "idx_wishlist_support_supporter",
                        columnList = "supporter"
                )
        }
)
public class WishlistSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String wishlist;

    @NotBlank
    @Column(nullable = false)
    private String supporter;

    @Min(1)
    @Column(nullable = false)
    private BigDecimal amount;

    private String supporterName;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}