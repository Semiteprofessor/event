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
}