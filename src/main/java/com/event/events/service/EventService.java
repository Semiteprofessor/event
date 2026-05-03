package com.event.events.service;

import com.event.events.dto.request.CreateEventRequest;
import com.event.events.dto.request.EditEventRequest;
import com.event.events.enums.AttendeeStatus;
import com.event.events.exception.AuthException;
import com.event.events.model.Event;
import com.event.events.model.SavedEvent;
import com.event.events.model.User;
import com.event.events.model.embeded.Attendee;
import com.event.events.repository.EventRepository;
import com.event.events.repository.SavedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final SavedEventRepository savedEventRepository;
    private final EmailService emailService;

    public Event createEvent(
            CreateEventRequest request,
            User user
    ) {

        String slug = request.getName()
                .toLowerCase()
                .replace(" ", "-");

        Event event = Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .slug(slug + "-" + System.currentTimeMillis())
                .organizerEmail(user.getEmail())
                .userId(user.getId())
                .build();

        Event saved = eventRepository.save(event);

        emailService.sendEventCreatedEmail(
                request.getOrganizerEmail(),
                "Ganusi: Event Created",
                event.getName(),
                event.getDate().toString(),
                event.getStartTime(),
                "Organizer"
        );

        return saved;
    }

    public Event editEvent(
            String id,
            EditEventRequest request,
            User user
    ) {

        Event event = getEvent(id);

        boolean allowed =
                user.getRole().name().equalsIgnoreCase("admin")
                        || user.getRole().name().equalsIgnoreCase("super_admin")
                        || event.getOrganizerEmail().equals(user.getEmail());

        if (!allowed) {
            throw new AuthException(403, "Forbidden");
        }

        event.setName(request.getName());
        event.setDescription(request.getDescription());

        return eventRepository.save(event);
    }

    public void deleteEvent(
            String id,
            User user
    ) {

        Event event = getEvent(id);

        boolean allowed =
                user.getRole().name().equalsIgnoreCase("admin")
                        || user.getRole().name().equalsIgnoreCase("super_admin")
                        || event.getOrganizerEmail().equals(user.getEmail());

        if (!allowed) {
            throw new AuthException(403, "Forbidden");
        }

        eventRepository.delete(event);
    }

    public Event getEvent(String id) {

        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new AuthException(
                                404,
                                "Event not found"
                        ));
    }

    public List<Event> getAllEvents(
            String search,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return eventRepository
                .findByNameContainingIgnoreCase(
                        search,
                        pageable
                );
    }

    public List<Event> getEventsByUser(String userId) {

        return eventRepository.findByUserId(userId);
    }

    public Event registerForEvent(
            String eventId,
            String userId,
            String email
    ) {

        Event event = getEvent(eventId);

        boolean exists = event.getAttendees()
                .stream()
                .anyMatch(a ->
                        a.getUserId().equals(userId));

        if (exists) {
            throw new AuthException(
                    400,
                    "Already registered"
            );
        }

        event.getAttendees()
                .add(
                        Attendee.builder()
                                .userId(userId)
                                .email(email)
                                .status(AttendeeStatus.ABSENT)
                                .build()
                );

        return eventRepository.save(event);
    }

    public Event takeAttendance(
            String eventId,
            String userId,
            String status
    ) {

        Event event = getEvent(eventId);

        Attendee attendee = event.getAttendees()
                .stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() ->
                        new AuthException(
                                404,
                                "Attendee not found"
                        ));

        try {
            AttendeeStatus attendeeStatus =
                    AttendeeStatus.valueOf(status.toUpperCase());

            attendee.setStatus(attendeeStatus);

        } catch (IllegalArgumentException ex) {
            throw new AuthException(
                    400,
                    "Invalid attendance status"
            );
        }

        return eventRepository.save(event);
    }

    public Integer totalRegistered(
            String eventId
    ) {

        Event event = getEvent(eventId);

        return event.getAttendees().size();
    }

    public SavedEvent saveEvent(
            String eventId,
            String userId
    ) {

        boolean exists =
                savedEventRepository
                        .existsByGuestAndEvent(
                                userId,
                                eventId
                        );

        if (exists) {
            throw new AuthException(
                    400,
                    "Already saved"
            );
        }

        SavedEvent saved = SavedEvent.builder()
                .guest(userId)
                .event(eventId)
                .build();

        return savedEventRepository.save(saved);
    }

    public String unsaveEvent(
            String eventId,
            String userId
    ) {

        savedEventRepository
                .deleteByGuestAndEvent(
                        userId,
                        eventId
                );

        return "Unsaved";
    }

    public List<SavedEvent> getSavedEvents(
            String userId
    ) {

        return savedEventRepository.findByGuest(userId);
    }

    public Boolean isEventSaved(
            String eventId,
            String userId
    ) {

        return savedEventRepository
                .existsByGuestAndEvent(
                        userId,
                        eventId
                );
    }

    public List<Event> getTrendingEvents() {

        return eventRepository.findAll()
                .stream()
                .sorted(
                        Comparator.comparingInt(
                                e -> e.getAttendees().size()
                        )
                )
                .limit(10)
                .toList();
    }
}