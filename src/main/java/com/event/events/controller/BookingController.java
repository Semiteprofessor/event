package com.event.events.controller;

import com.event.events.dto.request.BookingRequest;
import com.event.events.dto.request.InstallmentPaymentRequest;
import com.event.events.dto.response.ApiResponse;
import com.event.events.model.Booking;
import com.event.events.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse> createBooking(
            @Valid @RequestBody BookingRequest request
    ) {

        Booking booking = bookingService.createBooking(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Booking created successfully",
                        booking
                ));
    }

    @PatchMapping("/installment")
    public ResponseEntity<ApiResponse> payInstallment(
            @Valid @RequestBody InstallmentPaymentRequest request,
            @AuthenticationPrincipal(expression = "email") String email
    ) {

        Booking booking =
                bookingService.payInstallment(request, email);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Installment payment updated successfully",
                        booking
                )
        );
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse> getBookingById(
            @PathVariable String bookingId
    ) {

        Booking booking =
                bookingService.getBookingById(bookingId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Booking retrieved successfully",
                        booking
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyBookings(
            @AuthenticationPrincipal(expression = "email") String email
    ) {

        List<Booking> bookings =
                bookingService.getBookingsByUser(email);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Bookings retrieved successfully",
                        bookings
                )
        );
    }
}