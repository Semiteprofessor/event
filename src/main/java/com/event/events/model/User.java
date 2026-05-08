package com.event.events.model;

import com.event.events.enums.Provider;
import com.event.events.enums.Role;
import com.event.events.enums.UserType;
import com.event.events.model.embeded.Notifications;
import com.event.events.model.embeded.VendorOnboarding;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.guest;

    @Builder.Default
    private boolean isEmailVerified = false;

    @Column(length = 2000)
    private String refreshToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date refreshTokenExpires;

    private String resetToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date resetTokenExpires;

    @Builder.Default
    private boolean isAdmin = false;

    @Builder.Default
    private String profileImage = "";

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserType userType = UserType.FREE;

    @Builder.Default
    private boolean isOnboarded = false;

    @Embedded
    private VendorOnboarding onboarding;

    @Embedded
    @Builder.Default
    private Notifications notifications = new Notifications();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Provider provider = Provider.UNKNOWN;

    private String providerId;

    private String avatar;

    @Builder.Default
    private boolean isBanned = false;

    @Builder.Default
    private String banReason = "";

    @Temporal(TemporalType.TIMESTAMP)
    private Date bannedUntil;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}