package com.event.events.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private int status;
    private Object body;
    private String token;
}
