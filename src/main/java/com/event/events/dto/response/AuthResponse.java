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
}