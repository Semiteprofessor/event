package com.event.events.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InstallmentPaymentRequest {

    @NotBlank
    private String bookingId;

    @NotBlank
    private String ticketName;

    @NotNull
    private Double amount;

    private String reference;
}