package com.event.events.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingTicketRequest {

    @NotBlank(message = "Ticket type is required")
    private String type;

    @NotNull(message = "Ticket count is required")
    @Min(value = 1, message = "Minimum ticket count is 1")
    private Integer count;

    private boolean installment;
}