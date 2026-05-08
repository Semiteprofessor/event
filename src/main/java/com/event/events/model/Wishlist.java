package com.event.events.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
        name = "wishlists",
        indexes = {
                @Index(
                        name = "idx_wishlist_event",
                        columnList = "event"
                ),
                @Index(
                        name = "idx_wishlist_user_email",
                        columnList = "userEmail"
                )
        }
)
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String event;

    @Email
    @Column(nullable = false)
    private String userEmail;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String brand;

    private Integer quantity;

    private String size;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "wishlist_images",
            joinColumns = @JoinColumn(name = "wishlist_id")
    )
    @Column(name = "image_url")
    private List<String> images = List.of();

    @Column(nullable = false)
    private BigDecimal amountRaised = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}