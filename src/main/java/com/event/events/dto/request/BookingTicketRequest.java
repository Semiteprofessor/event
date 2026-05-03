package com.event.events.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookingTicketRequest {

    @NotBlank(message = "Ticket type is required")
    private String type;

    @NotNull(message = "Ticket count is required")
    @Positive(message = "Ticket count must be at least 1")
    private Integer count;

    private boolean installment;
}