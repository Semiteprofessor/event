package com.event.events.repository;

import com.event.events.model.SavedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SavedEventRepository extends JpaRepository<SavedEvent, String> {

    List<SavedEvent> findByGuest(String guestId);

    List<SavedEvent> findByEvent(String eventId);


    boolean existsByGuestAndEvent(
            String guest,
            String event
    );

    void deleteByGuestAndEvent(
            String guest,
            String event
    );

}
