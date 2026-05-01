package com.event.events.service;

import com.event.events.dto.request.LoginRequest;
import com.event.events.dto.request.RegisterRequest;
import com.event.events.dto.response.ApiResponse;
import com.event.events.dto.response.AuthResponse;
import com.event.events.model.Otp;
import com.event.events.model.User;
import com.event.events.repository.OtpRepository;
import com.event.events.repository.UserRepository;
import com.event.events.util.OtpUtil;
import com.event.events.util.PasswordUtil;
import com.event.events.util.TokenUtil;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
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
        try {
            User user = getUserOrFail(request.getEmail());

            validatePassword(request.getPassword(), user);

            if (!user.isEmailVerified()) {
                resendOtpInternal(user);
                return forbidden("Please verify your email. OTP sent.");
            }

            validateRole(user);

            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            persistRefreshToken(user, refreshToken);

            log.info("Login successful for {} (role: {})", user.getEmail(), user.getRole());

            user.setPassword(null);

            return success("Login successful", user, accessToken, refreshToken, user.getRole());

        } catch (AuthException ex) {
            return ex.toResponse();
        } catch (Exception ex) {
            log.error("Login error", ex);
            return serverError();
        }
    }

    @Transactional
    public AuthResponse registerUser(RegisterRequest request) {
        try {
            validatePasswordPresence(request.getPassword());

            Optional<User> existing = userRepository.findByEmail(request.getEmail());

            if (existing.isPresent()) {
                return handleExistingUser(existing.get(), request.getName());
            }

            User user = createNewUser(request);

            sendAndPersistOtp(user);

            log.info("User registered: {}", user.getEmail());

            return created(
                    "Registration successful. Please verify your email.",
                    user
            );

        } catch (AuthException ex) {
            return ex.toResponse();
        } catch (Exception ex) {
            log.error("Registration error", ex);
            return serverError();
        }
    }

    public AuthResponse verifyEmail(String email, String otp) {
        try {
            Otp record = validateOtp(email, otp);

            if (isOtpExpired(record)) {
                resendOtp(email);
                return unauthorized("OTP expired. New OTP sent.");
            }

            User user = getUserOrFail(email);
            user.setEmailVerified(true);
            userRepository.save(user);

            otpRepository.deleteByEmail(email);

            String token = jwtService.generateToken(user);

            log.info("User verified: {}", email);

            return success("Account verified successfully!", null, token, null, null);

        } catch (AuthException ex) {
            return ex.toResponse();
        } catch (Exception ex) {
            log.error("Verification error", ex);
            return serverError();
        }
    }

    public AuthResponse forgotPassword(String email) {
        try {
            validateEmailFormat(email);

            Optional<User> userOpt = userRepository.findByEmail(email);

            // Do not expose user existence
            if (userOpt.isEmpty()) {
                return ok("Check your mail if registered.");
            }

            User user = userOpt.get();

            String token = TokenUtil.generateSecureToken(64);

            user.setResetToken(token);
            userRepository.save(user);

            String link = buildResetLink(token);

            emailService.sendOtp(email, link, user.getName(), "Password Reset");

            return ok("Password reset link sent.");

        } catch (Exception ex) {
            log.error("Forgot password error", ex);
            return serverError();
        }
    }

    public AuthResponse refreshToken(String refreshToken) {
        try {
            String userId = jwtService.extractUserIdFromRefreshToken(refreshToken);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AuthException(401, "User not found"));

            validateStoredRefreshToken(user, refreshToken);

            String accessToken = jwtService.generateToken(user);

            return success("Token refreshed", null, accessToken, null, null);

        } catch (AuthException ex) {
            return ex.toResponse();
        } catch (Exception ex) {
            log.warn("Invalid refresh token attempt");
            return unauthorized("Invalid or expired refresh token");
        }
    }

    public AuthResponse resendOtp(String email) {
        try {
            User user = getUserOrFail(email);

            resendOtpInternal(user);

            return ok("OTP resent successfully.");

        } catch (AuthException ex) {
            return ex.toResponse();
        } catch (Exception ex) {
            log.error("Resend OTP error", ex);
            return serverError();
        }
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
        return ok("OTP resent for verification");
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
                .role(role)
                .isAdmin(isAdmin)
                .emailVerified(false)
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
        entity.setOtpType("REGISTRATION");
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


}