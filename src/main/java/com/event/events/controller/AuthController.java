package com.event.events.controller;

import com.event.events.dto.request.*;
import com.event.events.dto.response.ApiResponse;
import com.event.events.dto.response.AuthResponse;
import com.event.events.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
                .header("Authorization", "Bearer " + response.getToken())
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

    // ✅ RESET PASSWORD
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

    // ✅ RESEND OTP
    @PostMapping("/resend-otp/{email}")
    public ResponseEntity<?> resendOtp(@PathVariable String email) {

        AuthResponse response = authService.resendVerifyOtp(email);

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getBody());
    }

    // ✅ LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestAttribute("userId") String userId) {

        AuthResponse response = authService.logoutUser(userId);

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getBody());
    }

    // ✅ SOCIAL AUTH CALLBACK
    @PostMapping("/social/callback")
    public ResponseEntity<?> socialAuth(@RequestBody SocialUserRequest request) {

        if (request.getEmail() == null) {
            log.warn("Social auth callback missing email");
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Missing user profile data"));
        }

        AuthResponse response = authService.socialAuth(request);

        return ResponseEntity
                .status(response.getStatus())
                .body(response.getBody());
    }
}
