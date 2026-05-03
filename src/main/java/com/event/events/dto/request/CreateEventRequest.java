package com.event.events.dto.request;

import com.event.events.model.embeded.TicketType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateEventRequest {

    @NotBlank(message = "Event name is required")
    private String name;

    @Size(max = 5000, message = "Description is too long")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Event date is required")
    private Instant date;

    @NotBlank(message = "Start time is required")
    private String startTime;

    private String endTime;

    @NotBlank(message = "Organizer email is required")
    @Email(message = "Invalid organizer email")
    private String organizerEmail;

    private String hostEmail;

    private String posterEmail;

    private boolean isPrivate = false;

    private boolean allowInstallment = false;

    @Min(value = 1, message = "Installment count must be at least 1")
    private Integer numberOfInstallments;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal installmentPercentage;

    @Size(max = 10, message = "Maximum 10 media files allowed")
    private List<String> media = new ArrayList<>();

    @Valid
    @NotEmpty(message = "At least one ticket type is required")
    private List<TicketType> ticketTypes = new ArrayList<>();

    @Size(max = 50)
    private List<String> guests = new ArrayList<>();

    @Size(max = 100)
    private List<String> tags = new ArrayList<>();
}