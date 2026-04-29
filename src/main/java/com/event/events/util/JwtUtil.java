package com.event.events.util;

import com.event.events.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String jwtSecret = System.getenv("JWT_SECRET");
    private final String refreshSecret = System.getenv("JWT_REFRESH_SECRET");

    private final Key accessKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    private final Key refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());

    public String extractUserIdFromRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", String.class);
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("isAdmin", user.isAdmin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
