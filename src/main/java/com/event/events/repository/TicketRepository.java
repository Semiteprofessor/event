package com.event.events.repository;

import com.event.events.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository
        extends JpaRepository<Ticket, String> {

    List<Ticket> findByUserEmail(String userEmail);

    Optional<Ticket> findByQrSlug(String qrSlug);

    List<Ticket> findByBooking(String booking);

    List<Ticket> findByEvent(String event);

    List<Ticket> findByCheckedIn(boolean checkedIn);
}