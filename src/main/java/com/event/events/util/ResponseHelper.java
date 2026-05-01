package com.event.events.util;

import com.event.events.dto.response.ApiResponse;
import com.event.events.dto.response.AuthResponse;
import org.springframework.http.HttpStatus;

public final class ResponseHelper {

    private ResponseHelper() {}

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static ApiResponse<Void> ok(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static AuthResponse auth(
            HttpStatus status,
            String message,
            Object data,
            String accessToken,
            String refreshToken,
            String role
    ) {
        return new AuthResponse(
                status.value(),
                ok(message, data),
                accessToken,
                refreshToken,
                role
        );
    }

    public static AuthResponse authOk(String message, Object data) {
        return auth(HttpStatus.OK, message, data, null, null, null);
    }

    public static AuthResponse created(String message, Object data) {
        return auth(HttpStatus.CREATED, message, data, null, null, null);
    }

    public static AuthResponse forbidden(String message) {
        return auth(HttpStatus.FORBIDDEN, message, null, null, null, null);
    }

    public static AuthResponse unauthorized(String message) {
        return auth(HttpStatus.UNAUTHORIZED, message, null, null, null, null);
    }

    public static AuthResponse serverError() {
        return auth(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                null,
                null,
                null,
                null
        );
    }
}