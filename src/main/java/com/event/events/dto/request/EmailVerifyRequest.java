package com.event.events.dto.request;

import lombok.Data;

@Data
public class EmailVerifyRequest {
    private String email;
    private String otp;
}
