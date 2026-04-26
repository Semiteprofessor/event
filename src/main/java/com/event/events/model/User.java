package com.event.events.model;

import com.event.events.constant.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private String phone;
    private String bio;

    private Role role = Role.GUEST;

    private boolean isEmailVerified = false;

    private String refreshToken;
    private Date refreshTokenExpires;

    private String resetToken;
    private Date resetTokenExpires;

    private boolean isAdmin = false;
    private String profileImage = "";

    private UserType userType = UserType.FREE;

    private boolean isOnboarded = false;

    private VendorOnboarding onboarding;

    private Notifications notifications = new Notifications();

    private Provider provider = Provider.UNKNOWN;
    private String providerId;

    private String avatar;

    private boolean isBanned = false;
    private String banReason = "";
    private Date bannedUntil;

    private Date createdAt;
    private Date updatedAt;
}
