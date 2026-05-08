package com.event.events.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookingTicketRequest {

    @NotBlank
    private String type;

    @Min(1)
    private Integer count = 1;

    private boolean installment;
}