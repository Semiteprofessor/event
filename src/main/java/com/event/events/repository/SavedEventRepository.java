package com.event.events.repository;

import com.event.events.model.SavedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SavedEventRepository extends MongoRepository<SavedEvent, String> {

    List<SavedEvent> findByGuest(String guestId);

    List<SavedEvent> findByEvent(String eventId);

    boolean existsByGuestAndEvent(String guestId, String eventId);
}
