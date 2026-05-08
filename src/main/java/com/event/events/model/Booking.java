package com.event.events.model;

import com.event.events.enums.BookingStatus;
import com.event.events.enums.VendorStatus;
import com.event.events.model.embeded.BookingTicket;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String event;

    private Instant eventDate;

    private String userEmail;

    private String posterEmail;

    @ElementCollection
    @CollectionTable(
            name = "booking_tickets",
            joinColumns = @JoinColumn(name = "booking_id")
    )
    @Builder.Default
    private List<BookingTicket> tickets = new ArrayList<>();

    private Integer ticketsCount;

    private Double totalAmount;

    @ElementCollection
    @CollectionTable(
            name = "generated_tickets",
            joinColumns = @JoinColumn(name = "booking_id")
    )
    @Column(name = "ticket_code")
    @Builder.Default
    private List<String> ticketsGenerated = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BookingStatus status = BookingStatus.PAID;

    private String vendor;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VendorStatus vendorStatus = VendorStatus.PENDING;

    @ElementCollection
    @CollectionTable(
            name = "booking_registered_forms",
            joinColumns = @JoinColumn(name = "booking_id")
    )
    @Column(name = "form_value", columnDefinition = "TEXT")
    @Builder.Default
    private List<String> registeredForm = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}