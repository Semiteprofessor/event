package com.event.events.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateEventRequest {

    @NotBlank
    private String name;

    private String description;
}