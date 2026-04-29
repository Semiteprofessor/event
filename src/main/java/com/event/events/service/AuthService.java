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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthResponse loginUser(LoginRequest request) {

        try {
            String email = request.getEmail();
            String password = request.getPassword();

            Optional<User> userOpt = userRepository.findByEmail(email);

            if (userOpt.isEmpty()) {
                log.warn("Login failed — user not found: {}", email);
                return new AuthResponse(401,
                        new ApiResponse(false, "Invalid credentials"),
                        null);
            }

            User user = userOpt.get();

            // 🔐 Password check
            if (!PasswordUtil.matches(password, user.getPassword())) {
                log.warn("Invalid password for email: {}", email);
                return new AuthResponse(401,
                        new ApiResponse(false, "Invalid credentials"),
                        null);
            }

            // 📧 Email not verified
            if (!user.isEmailVerified()) {

                String otp = OtpUtil.generateOtp(5);

                Otp otpEntity = new Otp();
                otpEntity.setEmail(email);
                otpEntity.setOtp(otp);
                otpEntity.setOtpType("REGISTRATION");
                otpEntity.setUpdatedAt(new Date());

                otpRepository.save(otpEntity);

                emailService.sendOtp(email, otp, user.getName());

                log.info("Unverified email for {}. OTP resent.", email);

                return new AuthResponse(403,
                        new ApiResponse(false,
                                "Please verify your email. OTP sent."),
                        null);
            }

            // 🚫 Role check
            if (!List.of("guest", "vendor", "admin", "super admin")
                    .contains(user.getRole())) {

                log.error("Unauthorized role for {}: {}", email, user.getRole());

                return new AuthResponse(403,
                        new ApiResponse(false, "Access denied"),
                        null);
            }

            // 🔐 Generate tokens
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            user.setRefreshToken(refreshToken);
            user.setRefreshTokenExpires(
                    new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000)
            );

            userRepository.save(user);

            log.info("Login successful for {} (role: {})", email, user.getRole());

            user.setPassword(null); // hide password

            return new AuthResponse(
                    200,
                    new ApiResponse(true, "Login successful", user),
                    accessToken,
                    refreshToken,
                    user.getRole()
            );

        } catch (Exception err) {
            log.error("Login error: {}", err.getMessage());

            return new AuthResponse(
                    500,
                    new ApiResponse(false, "Internal Server Error"),
                    null
            );
        }
    }

    @Transactional
    public AuthResponse registerUser(RegisterRequest request) {

        try {
            String name = request.getName();
            String email = request.getEmail();
            String password = request.getPassword();
            String requestedRole = request.getRole();

            if (password == null || password.isBlank()) {
                return new AuthResponse(
                        401,
                        new ApiResponse(false, "Password is required"),
                        null
                );
            }

            Optional<User> existingUserOpt = userRepository.findByEmail(email);
            String otp = OtpUtil.generateOtp(5);

            // 🔁 Existing user
            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();

                if (existingUser.isEmailVerified()) {
                    return new AuthResponse(
                            403,
                            new ApiResponse(false,
                                    "User already exists and verified"),
                            null
                    );
                }

                // 🔄 Update OTP
                Otp otpEntity = otpRepository
                        .findByEmail(email)
                        .orElse(new Otp());

                otpEntity.setEmail(email);
                otpEntity.setOtp(otp);
                otpEntity.setOtpType("REGISTRATION");

                otpRepository.save(otpEntity);

                emailService.sendOtp(email, otp, name);

                return new AuthResponse(
                        200,
                        new ApiResponse(true, "OTP resent for verification"),
                        null
                );
            }

            // 👑 Role assignment
            long userCount = userRepository.count();

            String assignedRole =
                    userCount == 0
                            ? "super admin"
                            : (requestedRole != null ? requestedRole : "guest");

            boolean isAdmin =
                    assignedRole.equals("admin") || assignedRole.equals("super admin");

            // 👤 Create user
            User newUser = User.builder()
                    .name(name)
                    .email(email)
                    .password(PasswordUtil.encode(password))
                    .role(assignedRole)
                    .isAdmin(isAdmin)
                    .emailVerified(false)
                    .build();

            userRepository.save(newUser);

            // 🔐 Save OTP
            Otp otpEntity = new Otp();
            otpEntity.setOtp(otp);
            otpEntity.setName(name);
            otpEntity.setEmail(email);
            otpEntity.setOtpType("REGISTRATION");

            otpRepository.save(otpEntity);

            // 📧 Send email
            emailService.sendOtp(email, otp, name);

            log.info("{} registered successfully: {}", assignedRole, email);

            return new AuthResponse(
                    201,
                    new ApiResponse(
                            true,
                            "Registration successful as " + assignedRole +
                                    ". Please verify your email.",
                            newUser
                    ),
                    null
            );

        } catch (Exception err) {
            log.error("Registration failed for {}: {}", request.getEmail(), err.getMessage());

            return new AuthResponse(
                    500,
                    new ApiResponse(false, "Internal Server Error"),
                    null
            );
        }
    }

    public class TokenService {

        private final UserRepository userRepository;
        private final JwtService jwtService;

        public String refreshToken(String token) {

            if (token == null) {
                throw new RuntimeException("Refresh token missing");
            }

            Claims payload;

            try {
                payload = jwtService.verifyRefreshToken(token);
            } catch (Exception e) {
                throw new RuntimeException("Invalid or expired refresh token");
            }

            String userId = payload.getSubject();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!token.equals(user.getRefreshToken())) {
                throw new RuntimeException("Refresh token invalidated");
            }

            return jwtService.generateToken(user);
        }
    }

    public AuthResponse verifyEmail(String email, String otp) {

        try {
            Optional<Otp> otpRecordOpt = otpRepository
                    .findByEmailAndOtpAndOtpType(email, otp, "REGISTRATION");

            if (otpRecordOpt.isEmpty()) {
                log.error("Invalid or mismatched OTP for email: {}", email);
                return new AuthResponse(
                        401,
                        new ApiResponse(false,
                                "The provided OTP does not match. Please try again or request a new one."),
                        null
                );
            }

            Otp otpRecord = otpRecordOpt.get();

            // ⏱️ Check expiration (5 minutes)
            long diffMinutes = Duration.between(
                    otpRecord.getUpdatedAt().toInstant(),
                    Instant.now()
            ).toMinutes();

            if (diffMinutes > 5) {

                String newOtp = OtpUtil.generateOtp(5);

                otpRecord.setOtp(newOtp);
                otpRepository.save(otpRecord);

                log.warn("OTP expired for {}. New OTP generated.", email);

                return new AuthResponse(
                        401,
                        new ApiResponse(false,
                                "Your OTP has expired. A new one has been sent to your email."),
                        null
                );
            }

            // ✅ Update user
            Optional<User> userOpt = userRepository.findByEmail(email);

            if (userOpt.isEmpty()) {
                log.error("User not found for email: {}", email);
                return new AuthResponse(
                        404,
                        new ApiResponse(false, "User not found."),
                        null
                );
            }

            User user = userOpt.get();
            user.setEmailVerified(true);
            userRepository.save(user);

            // 🧹 Delete OTP
            otpRepository.deleteByEmail(email);

            // 🔐 Generate JWT
            String token = jwtService.generateToken(user);

            log.info("User {} verified and JWT generated.", email);

            return new AuthResponse(
                    200,
                    new ApiResponse(true, "Account verified successfully!"),
                    token
            );

        } catch (Exception err) {

            log.error("Email verification failed for {}: {}", email, err.getMessage());

            return new AuthResponse(
                    500,
                    new ApiResponse(false, "Internal Server Error"),
                    null
            );
        }
    }
}
