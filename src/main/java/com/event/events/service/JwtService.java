package com.event.events.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("isAdmin", user.isAdmin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}