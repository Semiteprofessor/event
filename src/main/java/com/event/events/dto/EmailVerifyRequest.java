package com.event.events.dto;

import lombok.Data;

@Data
public class EmailVerifyRequest {
    private String email;
    private String otp;
}
