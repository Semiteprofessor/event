package com.event.events.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private int status;
    private Object body;
    private String token;

    public AuthResponse(int value, ApiResponse success, String accessToken, String refreshToken, String role) {
    }

    public AuthResponse(int value, ApiResponse success, Object accessToken) {
    }
}
