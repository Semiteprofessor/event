package com.event.events.repository;

import com.event.events.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository
        extends JpaRepository<Event, String> {

    Event findBySlug(String slug);

    List<Event> findByUserId(String userId);

    List<Event> findByNameContainingIgnoreCase(
            String search,
            Pageable pageable
    );
}