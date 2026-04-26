package com.event.events.repository;

import com.event.events.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
    Event findBySlug(String slug);
}
