package com.event.events.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateGuestProfileRequest {

    @NotBlank
    private String name;

    private String phone;

    private String address;

    private String profileImage;
}