package com.event.events.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InstallmentEmailRequest {

    @NotBlank
    private String userName;

    @Email
    @NotBlank
    private String email;

    private String subject;

    @NotBlank
    private String eventName;

    @NotBlank
    private String ticketName;

    private double amountPaid;
    private double totalPaid;
    private double remainingAmount;
    private int installmentsPaid;
    private int numberOfInstallments;

    @NotBlank
    private String templateFile;

    private String time;
}