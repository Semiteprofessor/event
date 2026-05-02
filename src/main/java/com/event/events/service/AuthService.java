package com.event.events.service;

import com.event.events.dto.request.LoginRequest;
import com.event.events.dto.request.RegisterRequest;
import com.event.events.dto.response.ApiResponse;
import com.event.events.dto.response.AuthResponse;
import com.event.events.enums.OtpType;
import com.event.events.enums.Role;
import com.event.events.exception.AuthException;
import com.event.events.model.Otp;
import com.event.events.model.User;
import com.event.events.repository.OtpRepository;
import com.event.events.repository.UserRepository;
import com.event.events.util.OtpUtil;
import com.event.events.util.PasswordUtil;
import com.event.events.util.ResponseHelper;
import com.event.events.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final List<String> ALLOWED_ROLES =
            List.of("guest", "vendor", "admin", "super admin");

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthResponse loginUser(LoginRequest request) {

        User user = getUserOrFail(request.getEmail());

        validatePassword(request.getPassword(), user);

        if (!user.isEmailVerified()) {
            resendOtpInternal(user);
            throw new AuthException(403, "Please verify your email. OTP sent.");
        }

        validateRole(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        persistRefreshToken(user, refreshToken);

        log.info("Login successful for {}", user.getEmail());

        User safeUser = sanitizeUser(user);

        return ResponseHelper.auth(
                HttpStatus.OK,
                "Login successful",
                safeUser,
                accessToken,
                refreshToken,
                String.valueOf(user.getRole())
        );
    }

    @Transactional
    public AuthResponse registerUser(RegisterRequest request) {

        validatePasswordPresence(request.getPassword());

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    if (user.isEmailVerified()) {
                        throw new AuthException(403, "User already exists and is verified");
                    }
                    handleExistingUser(user, request.getName());
                });

        User user = createNewUser(request);

        sendAndPersistOtp(user);

        log.info("User registered: {}", user.getEmail());

        return ResponseHelper.created(
                "Registration successful. Please verify your email.",
                sanitizeUser(user)
        );
    }

    public AuthResponse verifyEmail(String email, String otp) {

        Otp record = validateOtp(email, otp);

        handleExpiredOtp(record, email);

        User user = getUserOrFail(email);

        activateUser(user);

        clearOtp(email);

        String token = jwtService.generateToken(user);

        log.info("User verified: {}", email);

        return ResponseHelper.auth(
                HttpStatus.OK,
                "Account verified successfully!",
                null,
                token,
                null,
                null
        );
    }

    public AuthResponse forgotPassword(String email) {

        validateEmailFormat(email);

        userRepository.findByEmail(email)
                .ifPresent(this::processPasswordReset);

        return AuthResponse.ok(
                "If the email exists, a reset link has been sent."
        );
    }

    public AuthResponse resetPassword(String token, String newPassword) {

        validateResetRequest(token, newPassword);

        userRepository.findByResetToken(token)
                .ifPresent(this::processPasswordReset);

        return AuthResponse.ok("If the token is valid, password has been reset.");
    }

    public AuthResponse refreshToken(String refreshToken) {

        String userId = jwtService.extractUserIdFromRefreshToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(401, "User not found"));

        validateStoredRefreshToken(user, refreshToken);

        String accessToken = jwtService.generateToken(user);

        return AuthResponse.success(
                "Token refreshed",
                null,
                accessToken,
                null,
                null
        );
    }

    public AuthResponse resendOtp(String email) {

        User user = getUserOrFail(email);

        resendOtpInternal(user);

        return AuthResponse.ok("OTP resent successfully.");
    }

    public AuthResponse logoutUser(User user) {

        if (user == null) {
            throw new AuthException(401, "User not authenticated");
        }

        user.setRefreshToken(null);
        user.setRefreshTokenExpires(null);

        userRepository.save(user);

        log.info("User logged out: {}", user.getEmail());

        return AuthResponse.ok("Logout successful");
    }

    public AuthResponse socialLogin(User oauthUser) {

        if (oauthUser == null || oauthUser.getEmail() == null) {
            throw new AuthException(400, "Invalid social auth data");
        }

        User user = userRepository.findByEmail(oauthUser.getEmail())
                .orElseGet(() -> createSocialUser(oauthUser));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        log.info("Social login successful: {}", user.getEmail());

        return AuthResponse.success(
                "Social login successful",
                user,
                accessToken,
                refreshToken,
                String.valueOf(user.getRole())
        );
    }

    private User createSocialUser(User oauthUser) {

        User newUser = User.builder()
                .name(oauthUser.getName())
                .email(oauthUser.getEmail())
                .password(null)
                .role(Role.GUEST)
                .isEmailVerified(true)
                .isAdmin(false)
                .build();

        return userRepository.save(newUser);
    }


    private User getUserOrFail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(401, "Invalid credentials"));
    }

    private void validatePassword(String raw, User user) {
        if (!PasswordUtil.matches(raw, user.getPassword())) {
            throw new AuthException(401, "Invalid credentials");
        }
    }

    private void validateRole(User user) {
        if (!ALLOWED_ROLES.contains(user.getRole())) {
            throw new AuthException(403, "Access denied");
        }
    }

    private void validatePasswordPresence(String password) {
        if (password == null || password.isBlank()) {
            throw new AuthException(400, "Password is required");
        }
    }

    private AuthResponse handleExistingUser(User user, String name) {
        if (user.isEmailVerified()) {
            throw new AuthException(403, "User already exists");
        }
        resendOtpInternal(user);
        return AuthResponse.ok("OTP resent for verification");
    }

    private User createNewUser(RegisterRequest req) {
        long count = userRepository.count();

        String role = count == 0 ? "super admin"
                : (req.getRole() != null ? req.getRole() : "guest");

        boolean isAdmin = role.contains("admin");

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(PasswordUtil.encode(req.getPassword()))
                .role(Role.valueOf(role))
                .isAdmin(isAdmin)
                .build();

        return userRepository.save(user);
    }

    private void resendOtpInternal(User user) {
        String otp = OtpUtil.generateOtp(5);
        saveOtp(user.getEmail(), otp);
        emailService.sendOtp(user.getEmail(), otp, user.getName());
    }

    private void sendAndPersistOtp(User user) {
        String otp = OtpUtil.generateOtp(5);
        saveOtp(user.getEmail(), otp);
        emailService.sendOtp(user.getEmail(), otp, user.getName());
    }

    private void saveOtp(String email, String otp) {
        Otp entity = otpRepository.findByEmail(email).orElse(new Otp());
        entity.setEmail(email);
        entity.setOtp(otp);
        entity.setOtpType(OtpType.REGISTRATION);
        otpRepository.save(entity);
    }

    private Otp validateOtp(String email, String otp) {
        return otpRepository.findByEmailAndOtpAndOtpType(email, otp, "REGISTRATION")
                .orElseThrow(() -> new AuthException(401, "Invalid OTP"));
    }

    private boolean isOtpExpired(Otp otp) {
        return Duration.between(
                otp.getUpdatedAt().toInstant(),
                Instant.now()
        ).toMinutes() > 5;
    }

    private void persistRefreshToken(User user, String token) {
        user.setRefreshToken(token);
        userRepository.save(user);
    }

    private void validateStoredRefreshToken(User user, String token) {
        if (!token.equals(user.getRefreshToken())) {
            throw new AuthException(401, "Refresh token invalidated");
        }
    }

    private String buildResetLink(String token) {
        return System.getenv("FRONTEND_REMOTE_URL")
                + "/auth/reset-password?token=" + token;
    }

    private User sanitizeUser(User user) {
        User safe = new User();

        safe.setId(user.getId());
        safe.setName(user.getName());
        safe.setEmail(user.getEmail());
        safe.setRole(user.getRole());
        safe.setEmailVerified(user.isEmailVerified());
        safe.setAdmin(user.isAdmin());

        return safe;
    }

    private void handleExpiredOtp(Otp record, String email) {
        if (!isOtpExpired(record)) return;

        resendOtp(email);
        throw new AuthException(401, "OTP expired. New OTP sent.");
    }

    private void activateUser(User user) {
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    private void clearOtp(String email) {
        otpRepository.deleteByEmail(email);
    }

    private void validateResetRequest(String token, String password) {
        if (token == null || token.isBlank()) {
            throw new AuthException(400, "Token is required");
        }

        if (password == null || password.isBlank()) {
            throw new AuthException(400, "Password is required");
        }
    }

    private void processPasswordReset(User user) {

        String token = TokenUtil.generateSecureToken(64);

        user.setResetToken(token);
        user.setResetTokenExpires(
                new Date(System.currentTimeMillis() + 1000 * 60 * 15)
        );

        userRepository.save(user);

        String link = buildResetLink(token);

        String message =
                "Hello " + user.getName() +
                        ", click the link below to reset your password:\n" + link;

        emailService.sendOtp(
                user.getEmail(),
                "Password Reset",
                message
        );

        log.info("Password reset email sent to {}", user.getEmail());
    }

    private void validateEmailFormat(String email) {
        if (email == null || !email.matches("^\\S+@\\S+\\.\\S+$")) {
            throw new AuthException(400, "Invalid email format");
        }
    }

}