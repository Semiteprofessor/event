package com.event.events.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
