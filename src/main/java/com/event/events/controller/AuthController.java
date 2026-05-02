package com.event.events.controller;

import com.event.events.dto.request.*;
import com.event.events.dto.response.ApiResponse;
import com.event.events.dto.response.AuthResponse;
import com.event.events.model.User;
import com.event.events.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {

        log.info("Incoming registration request for email: {}", request.getEmail());

        return ResponseEntity
                .status(authService.registerUser(request).getStatus())
                .body(authService.registerUser(request).getBody());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {

        log.info("Incoming login request for email: {}", request.getEmail());

        AuthResponse response = authService.loginUser(request);

        return ResponseEntity
                .status(response.getStatus())
                .header("Authorization", "Bearer " + response.getAccessToken())
                .body(response.getBody());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String cookieToken,
            @RequestHeader(value = "Authorization", required = false) String headerToken
    ) {

        log.info("Incoming refresh token request");

        String token = cookieToken != null
                ? cookieToken
                : (headerToken != null ? headerToken.replace("Bearer ", "") : null);

        if (token == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse(false, "Refresh token missing"));
        }

        AuthResponse response = authService.refreshToken(token);

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getBody());
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> emailVerify(@RequestBody EmailVerifyRequest request) {

        log.info("Incoming email verification for {}", request.getEmail());

        AuthResponse response =
                authService.verifyEmail(request.getEmail(), request.getOtp());

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getBody());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {

        AuthResponse response = authService.forgotPassword(request.getEmail());

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getBody());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestBody ResetPasswordRequest request
    ) {

        AuthResponse response =
                authService.resetPassword(token, request.getNewPassword());

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getBody());
    }

    @PostMapping("/resend-otp/{email}")
    public ResponseEntity<?> resendOtp(@PathVariable String email) {

        AuthResponse response = authService.resendOtp(email);

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getBody());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        AuthResponse response = authService.logoutUser(user);

        return ResponseEntity.status(response.getStatus())
                .body(response.getBody());
    }

    @PostMapping("/auth/social")
    public ResponseEntity<?> socialAuth(@RequestBody User oauthUser) {

        AuthResponse response = authService.socialLogin(oauthUser);

        return ResponseEntity.status(response.getStatus())
                .body(response.getBody());
    }
}
