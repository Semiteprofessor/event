package com.event.events.service;

import com.event.events.dto.request.BookingRequest;
import com.event.events.dto.request.BookingTicketRequest;
import com.event.events.dto.request.InstallmentPaymentRequest;
import com.event.events.enums.BookingStatus;
import com.event.events.enums.PaymentType;
import com.event.events.model.Booking;
import com.event.events.model.Event;
import com.event.events.model.embeded.BookingTicket;
import com.event.events.model.embeded.InstallmentDetails;
import com.event.events.model.Payment;
import com.event.events.model.embeded.TicketType;
import com.event.events.repository.BookingRepository;
import com.event.events.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;

    @Transactional
    public Booking createBooking(BookingRequest request) {

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        int totalTickets = 0;

        for (BookingTicketRequest ticketReq : request.getTickets()) {

            TicketType eventTicket = event.getTicketTypes().stream()
                    .filter(t -> t.getType().equalsIgnoreCase(ticketReq.getType()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            if (eventTicket.getRemaining() < ticketReq.getCount()) {
                throw new RuntimeException("Not enough tickets");
            }

            BigDecimal total =
                    eventTicket.getPrice()
                            .multiply(BigDecimal.valueOf(ticketReq.getCount()));

            eventTicket.setRemaining(
                    eventTicket.getRemaining() - ticketReq.getCount()
            );

            BookingTicket bookingTicket = BookingTicket.builder()
                    .type(ticketReq.getType())
                    .count(ticketReq.getCount())
                    .price(eventTicket.getPrice())
                    .totalAmount(total)
                    .paymentType(
                            ticketReq.isInstallment()
                                    ? PaymentType.INSTALLMENT
                                    : PaymentType.ONE_OFF
                    )
                    .build();

            if (event.isAllowInstallment() && ticketReq.isInstallment()) {

                InstallmentDetails details = new InstallmentDetails();

                int inst = event.getInstallmentConfig().getNumberOfInstallments();

                details.setNumberOfInstallments(inst);
                details.setInstallmentsPaid(0);
                details.setNumberOfInstallments(inst);
                details.setInstallmentsPaid(0);
                details.setTotalPaid(BigDecimal.ZERO);
                details.setRemainingAmount(total);
                details.setTotalPaid(BigDecimal.ZERO);
                details.setRemainingAmount(total);

                bookingTicket.setPaymentType(PaymentType.INSTALLMENT);
                bookingTicket.setInstallmentDetails(details);

            } else {
                bookingTicket.setPaymentType(PaymentType.ONE_OFF);
            }

            bookingTickets.add(bookingTicket);

            totalTickets += ticketReq.getCount();
        }

        eventRepository.save(event);

        Booking booking = new Booking();
        booking.setUserEmail(request.getUserEmail());
        booking.setEventId(event.getId());
        booking.setTickets(request.getTickets());
        booking.setTicketsCount(totalTickets);
        booking.setStatus(BookingStatus.PAID);
        booking.setCreatedAt(Instant.now());

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking payInstallment(InstallmentPaymentRequest request, String email) {

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        BookingTicket ticket = booking.getTickets().stream()
                .filter(t -> t.getType().equalsIgnoreCase(request.getTicketName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        InstallmentDetails details = ticket.getInstallmentDetails();

        BigDecimal amount = BigDecimal.valueOf(request.getAmount());

        details.setTotalPaid(details.getTotalPaid().add(amount));
        details.setInstallmentsPaid(details.getInstallmentsPaid() + 1);
        details.setRemainingAmount(
                details.getRemainingAmount().subtract(amount)
        );

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setReference(request.getReference());
        payment.setCreatedAt(Instant.now());

        details.getPayments().add(payment);

        booking.setStatus(
                details.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0
                        ? "paid"
                        : "partial"
        );

        Booking saved = bookingRepository.save(booking);

        try {
            emailService.sendInstallmentPaymentMail(
                    booking.getUserEmail(),
                    booking.getUserEmail(),
                    "Installment Update",
                    String.valueOf(booking.getEventId()),
                    ticket.getType(),
                    amount.doubleValue(),
                    details.getTotalPaid().doubleValue(),
                    details.getRemainingAmount().doubleValue(),
                    details.getInstallmentsPaid(),
                    details.getNumberOfInstallments(),
                    "installment.hbs",
                    LocalDateTime.now().toString()
            );
        } catch (Exception e) {
            log.error("Email failed", e);
        }

        return saved;
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public List<Booking> getMyBookings(String email) {
        return bookingRepository.findByUserEmail(email);
    }
}