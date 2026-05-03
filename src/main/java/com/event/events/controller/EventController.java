package com.event.events.controller;

import com.event.events.dto.request.CreateEventRequest;
import com.event.events.dto.request.EditEventRequest;
import com.event.events.dto.response.ApiResponse;
import com.event.events.model.Event;
import com.event.events.model.User;
import com.event.events.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createEvent(
            @Valid @RequestBody CreateEventRequest request,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Event created successfully",
                        eventService.createEvent(request, user)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> editEvent(
            @PathVariable String id,
            @Valid @RequestBody EditEventRequest request,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Event updated successfully",
                        eventService.editEvent(id, request, user)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteEvent(
            @PathVariable String id,
            @AuthenticationPrincipal User user
    ) {

        eventService.deleteEvent(id, user);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Event deleted successfully",
                        null
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getEvent(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Event fetched successfully",
                        eventService.getEvent(id)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllEvents(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Events fetched successfully",
                        eventService.getAllEvents(search, page, size)
                )
        );
    }

    @GetMapping("/my-events")
    public ResponseEntity<ApiResponse<?>> getMyEvents(
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "My events fetched successfully",
                        eventService.getEventsByUser(user.getId())
                )
        );
    }

    @PostMapping("/{eventId}/register")
    public ResponseEntity<ApiResponse<?>> registerForEvent(
            @PathVariable String eventId,
            @RequestParam String email,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Registered successfully",
                        eventService.registerForEvent(
                                eventId,
                                user.getId(),
                                email
                        )
                )
        );
    }

    @PostMapping("/{eventId}/attendance")
    public ResponseEntity<ApiResponse<?>> takeAttendance(
            @PathVariable String eventId,
            @RequestParam String userId,
            @RequestParam String status
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Attendance updated",
                        eventService.takeAttendance(
                                eventId,
                                userId,
                                status
                        )
                )
        );
    }

    @GetMapping("/{eventId}/total-registered")
    public ResponseEntity<ApiResponse<?>> totalRegistered(
            @PathVariable String eventId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Total registered fetched",
                        eventService.totalRegistered(eventId)
                )
        );
    }

    @PostMapping("/{eventId}/save")
    public ResponseEntity<ApiResponse<?>> saveEvent(
            @PathVariable String eventId,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Event saved",
                        eventService.saveEvent(eventId, user.getId())
                )
        );
    }

    @DeleteMapping("/{eventId}/unsave")
    public ResponseEntity<ApiResponse<?>> unsaveEvent(
            @PathVariable String eventId,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Event unsaved",
                        eventService.unsaveEvent(eventId, user.getId())
                )
        );
    }

    @GetMapping("/saved")
    public ResponseEntity<ApiResponse<?>> getSavedEvents(
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Saved events fetched",
                        eventService.getSavedEvents(user.getId())
                )
        );
    }

    @GetMapping("/{eventId}/saved")
    public ResponseEntity<ApiResponse<?>> isSaved(
            @PathVariable String eventId,
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Saved status fetched",
                        eventService.isEventSaved(eventId, user.getId())
                )
        );
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<?>> trending() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Trending events fetched",
                        eventService.getTrendingEvents()
                )
        );
    }
}