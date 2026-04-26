package com.event.events.model;

import com.event.events.enums.VendorStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    private String event;
    private Date eventDate;

    private String userEmail;
    private String posterEmail;

    private List<BookingTicket> tickets;

    private Integer ticketsCount;

    private Double totalAmount;

    private List<String> ticketsGenerated;

    private BookingStatus status = BookingStatus.PAID;

    private String vendor; // ObjectId → String

    private VendorStatus vendorStatus = VendorStatus.PENDING;

    private List<Object> registeredForm;

    private Date createdAt;
    private Date updatedAt;
}
