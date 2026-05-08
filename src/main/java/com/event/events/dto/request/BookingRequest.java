package com.event.events.dto.request;

import com.event.events.model.embeded.BookingTicket;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {

    @Email
    @NotBlank
    private String userEmail;

    @NotBlank
    private String event;

    @Valid
    @NotEmpty
    private List<BookingTicket> tickets;
}