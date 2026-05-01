package com.event.events.service;

import com.event.events.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String accessSecret;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${jwt.expiration}")
    private long accessExpiration;
    private static final long REFRESH_EXPIRATION = 7L * 24 * 60 * 60 * 1000;

    private Key getAccessKey() {
        return Keys.hmacShaKeyFor(accessSecret.getBytes());
    }

    private Key getRefreshKey() {
        return Keys.hmacShaKeyFor(refreshSecret.getBytes());
    }


    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(getAccessKey())
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(getRefreshKey())
                .compact();
    }


    public Claims extractAllClaims(String token, boolean isRefresh) {
        return Jwts.parserBuilder()
                .setSigningKey(isRefresh ? getRefreshKey() : getAccessKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserIdFromRefreshToken(String token) {
        return extractAllClaims(token, true).getSubject();
    }

    public String extractUserIdFromAccessToken(String token) {
        return extractAllClaims(token, false).getSubject();
    }

    /* ===================== VALIDATION ===================== */

    public boolean isTokenValid(String token, boolean isRefresh) {
        try {
            extractAllClaims(token, isRefresh);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}