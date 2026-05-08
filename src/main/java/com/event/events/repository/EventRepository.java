package com.event.events.repository;

import com.event.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, String> {

    Optional<Event> findBySlug(String slug);
}