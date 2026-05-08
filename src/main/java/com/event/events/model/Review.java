package com.event.events.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
        name = "reviews",
        indexes = {
                @Index(
                        name = "idx_review_vendor",
                        columnList = "vendor"
                ),
                @Index(
                        name = "idx_review_rating",
                        columnList = "rating"
                )
        }
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String vendor;

    @NotBlank
    @Column(nullable = false)
    private String reviewerName;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String reviewerEmail;

    private String reviewerAvatar;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int rating;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    private String eventName;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}