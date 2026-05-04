package com.event.events.util;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    private final Key accessKey;
    private final Key refreshKey;

    public JwtUtil(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.refresh-secret}") String refreshSecret
    ) {
        this.accessKey = buildKey(jwtSecret);
        this.refreshKey = buildKey(refreshSecret);
    }

    private Key buildKey(String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 characters long");
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}