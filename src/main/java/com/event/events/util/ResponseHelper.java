package com.event.events.util;

import com.event.events.dto.response.ApiResponse;
import com.event.events.dto.response.AuthResponse;
import org.springframework.http.HttpStatus;

public final class ResponseHelper {

    private ResponseHelper() {
        // Prevent instantiation
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data);
    }

    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, message);
    }

    public static AuthResponse authSuccess(
            String message,
            Object data,
            String accessToken,
            String refreshToken,
            String role
    ) {
        return new AuthResponse(
                HttpStatus.OK.value(),
                success(message, data),
                accessToken,
                refreshToken,
                role
        );
    }

    public static AuthResponse ok(String message) {
        return new AuthResponse(
                HttpStatus.OK.value(),
                success(message),
                null
        );
    }

    public static AuthResponse created(String message, Object data) {
        return new AuthResponse(
                HttpStatus.CREATED.value(),
                success(message, data),
                null
        );
    }

    public static AuthResponse unauthorized(String message) {
        return new AuthResponse(
                HttpStatus.UNAUTHORIZED.value(),
                error(message),
                null
        );
    }

    public static AuthResponse forbidden(String message) {
        return new AuthResponse(
                HttpStatus.FORBIDDEN.value(),
                error(message),
                null
        );
    }

    public static AuthResponse serverError() {
        return new AuthResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error("Internal Server Error"),
                null
        );
    }
}