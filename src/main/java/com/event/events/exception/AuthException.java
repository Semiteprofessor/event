package com.event.events.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final int status;

    public AuthException(int status, String message) {
        super(message);
        this.status = status;
    }

    public AuthException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}