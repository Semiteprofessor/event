package com.event.events.service;

import com.event.events.dto.response.ApiResponse;
import com.event.events.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
