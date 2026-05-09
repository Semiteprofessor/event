package com.event.events.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateGuestPasswordRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}