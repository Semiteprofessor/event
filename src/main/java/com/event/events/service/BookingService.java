package com.event.events.service;

import com.event.events.dto.request.BookingRequest;
import com.event.events.dto.request.InstallmentPaymentRequest;
import com.event.events.enums.PaymentType;
import com.event.events.exception.AuthException;
import com.event.events.model.Booking;
import com.event.events.model.Event;
import com.event.events.model.Ticket;
import com.event.events.model.embeded.BookingTicket;
import com.event.events.model.embeded.InstallmentDetails;
import com.event.events.model.Payment;
import com.event.events.model.embeded.TicketType;
import com.event.events.repository.BookingRepository;
import com.event.events.repository.EventRepository;
import com.event.events.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    @Transactional
    public Booking createBooking(BookingRequest request) {

        Event event = eventRepository.findById(request.getEvent())
                .orElseThrow(() ->
                        new AuthException(404, "Event not found"));

        List<TicketType> eventTickets = event.getTicketTypes();

        int totalCount = 0;

        for (BookingTicket bookingTicket : request.getTickets()) {

            TicketType eventTicket = eventTickets.stream()
                    .filter(t ->
                            t.getType()
                                    .equalsIgnoreCase(
                                            bookingTicket.getType()
                                    )
                    )
                    .findFirst()
                    .orElseThrow(() ->
                            new AuthException(
                                    400,
                                    "Ticket type not found"
                            ));

            int count = bookingTicket.getCount();

            if (eventTicket.getRemaining() < count) {
                throw new AuthException(
                        400,
                        "Not enough tickets available"
                );
            }

            BigDecimal total =
                    eventTicket.getPrice()
                            .multiply(BigDecimal.valueOf(count));

            bookingTicket.setPrice(eventTicket.getPrice());
            bookingTicket.setTotalAmount(total);

            if (bookingTicket.isInstallment()) {

                int installments =
                        event.getInstallmentConfig()
                                .getNumberOfInstallments();

                InstallmentDetails details =
                        new InstallmentDetails();

                details.setNumberOfInstallments(installments);

                details.setAmountPerInstallment(
                        total.divide(
                                BigDecimal.valueOf(installments)
                        )
                );

                details.setInstallmentsPaid(0);
                details.setRemainingAmount(total);
                details.setTotalPaid(BigDecimal.ZERO);
                details.setPayments(new ArrayList<>());

                bookingTicket.setPaymentType(
                        PaymentType.INSTALLMENT
                );

                bookingTicket.setInstallmentDetails(details);

            } else {
                bookingTicket.setPaymentType(
                        PaymentType.ONE_OFF
                );
            }

            eventTicket.setRemaining(
                    eventTicket.getRemaining() - count
            );

            totalCount += count;
        }

        eventRepository.save(event);

        Booking booking = Booking.builder()
                .userEmail(request.getUserEmail())
                .event(request.getEvent())
                .tickets(request.getTickets())
                .ticketsCount(totalCount)
                .createdAt(Instant.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        createTickets(saved);

        return saved;
    }

    @Transactional
    public Booking payInstallment(
            InstallmentPaymentRequest request,
            String email
    ) {

        Booking booking = bookingRepository.findById(
                        request.getBookingId()
                )
                .orElseThrow(() ->
                        new AuthException(404, "Booking not found"));

        BookingTicket ticket = booking.getTickets()
                .stream()
                .filter(t ->
                        t.getType().equalsIgnoreCase(
                                request.getTicketName()
                        ) && t.isInstallment()
                )
                .findFirst()
                .orElseThrow(() ->
                        new AuthException(
                                400,
                                "Installment ticket not found"
                        ));

        InstallmentDetails details =
                ticket.getInstallmentDetails();

        BigDecimal amount = BigDecimal.valueOf(request.getAmount());

        details.setTotalPaid(
                details.getTotalPaid().add(amount)
        );

        details.setInstallmentsPaid(
                details.getInstallmentsPaid() + 1
        );

        details.setRemainingAmount(
                details.getRemainingAmount().subtract(amount)
        );

        Payment payment = new Payment();

        payment.setAmount(amount);
        payment.setReference(request.getReference());
        payment.setCreatedAt(Instant.now());

        details.getPayments().add(payment);

        bookingRepository.save(booking);

        try {
            emailService.sendInstallmentPaymentMail(
                    booking.getUserEmail(),
                    booking.getUserEmail(),
                    "Installment Payment Update",
                    booking.getEvent().toString(),
                    ticket.getType(),
                    amount.doubleValue(),
                    details.getTotalPaid().doubleValue(),
                    details.getRemainingAmount().doubleValue(),
                    details.getInstallmentsPaid(),
                    details.getNumberOfInstallments(),
                    "installmentPaymentHistory.hbs",
                    getFormattedDateTime()
            );

        } catch (Exception ex) {
            log.error("Failed to send installment email", ex);
        }

        return booking;
    }

    private String getFormattedDateTime() {
        return java.time.LocalDateTime
                .now()
                .format(
                        java.time.format.DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd HH:mm:ss"
                        )
                );
    }

    public Booking getBookingById(String bookingId) {

        return bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new AuthException(
                                404,
                                "Booking not found"
                        ));
    }

    public List<Booking> getBookingsByUser(String email) {

        return bookingRepository.findByUserEmail(email);
    }

    private void createTickets(Booking booking) {

        for (BookingTicket ticket : booking.getTickets()) {

            for (int i = 0; i < ticket.getCount(); i++) {

                Ticket generated = Ticket.builder()
                        .booking(booking.getId())
                        .event(booking.getEvent())
                        .ticketType(ticket.getType())
                        .userEmail(booking.getUserEmail())
                        .price(ticket.getPrice())
                        .totalAmount(ticket.getTotalAmount())
                        .createdAt(Instant.now())
                        .build();

                ticketRepository.save(generated);
            }
        }
    }
}