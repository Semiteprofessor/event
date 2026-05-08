package com.event.events.model;

import com.event.events.enums.VendorStatus;
import com.event.events.model.embeded.NotificationPreference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "vendors",
        indexes = {
                @Index(
                        name = "idx_vendor_user",
                        columnList = "userId"
                ),
                @Index(
                        name = "idx_vendor_email",
                        columnList = "email",
                        unique = true
                ),
                @Index(
                        name = "idx_vendor_status",
                        columnList = "status"
                )
        }
)
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(name = "userId", nullable = false)
    private String user;

    @NotBlank
    @Column(nullable = false)
    private String businessName;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$")
    private String phone;

    @NotBlank
    @Column(nullable = false)
    private String category;

    @NotBlank
    @Column(nullable = false)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String avatar;

    @Embedded
    private NotificationPreference notificationPreference = new NotificationPreference();

    @ElementCollection
    @CollectionTable(
            name = "vendor_portfolio_items",
            joinColumns = @JoinColumn(name = "vendor_id")
    )
    @Column(name = "portfolio_item")
    private List<String> portfolioItems;

    @Min(0)
    private Double startingPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorStatus status = VendorStatus.PENDING;

    private Instant completedAt;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}