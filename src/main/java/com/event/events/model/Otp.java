package com.event.events.model;

import com.event.events.enums.OtpType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
        name = "otps",
        indexes = {
                @Index(
                        name = "idx_otp_email_type_updated",
                        columnList = "email, otpType, updatedAt"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_otp_email_type",
                        columnNames = {"email", "otpType"}
                )
        }
)
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private String otp;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OtpType otpType = OtpType.REGISTRATION;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}