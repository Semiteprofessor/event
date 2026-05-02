package com.event.events.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private int status;
    private ApiResponse<?> body;

    private String accessToken;
    private String refreshToken;
    private String role;

    public AuthResponse(int status, ApiResponse<?> body) {
        this.status = status;
        this.body = body;
    }

    public AuthResponse(int status, ApiResponse<?> body, String accessToken) {
        this.status = status;
        this.body = body;
        this.accessToken = accessToken;
    }

    public AuthResponse(int status,
                        ApiResponse<?> body,
                        String accessToken,
                        String refreshToken,
                        String role) {
        this.status = status;
        this.body = body;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }

    public static AuthResponse ok(String message) {
        return new AuthResponse(
                200,
                new ApiResponse(true, message),
                null,
                null,
                null
        );
    }

    public static AuthResponse created(String message, Object data) {
        return new AuthResponse(
                201,
                new ApiResponse(true, message, data),
                null,
                null,
                null
        );
    }

    public static AuthResponse unauthorized(String message) {
        return new AuthResponse(
                401,
                new ApiResponse(false, message),
                null,
                null,
                null
        );
    }

    public static AuthResponse success(
            String message,
            Object data,
            String accessToken,
            String refreshToken,
            String role
    ) {
        return new AuthResponse(
                200,
                new ApiResponse(true, message, data),
                accessToken,
                refreshToken,
                role
        );
    }
}