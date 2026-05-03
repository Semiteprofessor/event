package com.event.events.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {

    @Email
    @NotBlank
    private String userEmail;

    @NotNull
    private String eventId;

    @Valid
    @NotEmpty
    private List<BookingTicketRequest> tickets;
}