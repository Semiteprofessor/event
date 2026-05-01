package com.event.events.service;

import com.event.events.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String accessSecret;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    private Key getAccessKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(refreshSecret.getBytes());
    }

    /* ================= EXTRACT USER ID ================= */

    public String extractUserIdFromRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getRefreshKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // 👈 userId stored here
    }

    /* ================= GENERATE TOKENS ================= */

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .signWith(getAccessKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .signWith(getRefreshKey())
                .compact();
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, accessSecret)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, refreshSecret)
                .compact();
    }

    public Claims verifyRefreshToken(String token) {
        return Jwts.parser()
                .setSigningKey(refreshSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}