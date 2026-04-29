package com.event.events.service;

import com.event.events.model.User;
import com.event.events.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
