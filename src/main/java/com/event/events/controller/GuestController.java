package com.event.events.controller;

import com.event.events.dto.request.SendBookingRequest;
import com.event.events.dto.request.UpdateGuestPasswordRequest;
import com.event.events.dto.request.UpdateGuestProfileRequest;
import com.event.events.dto.response.ApiResponse;
import com.event.events.model.Booking;
import com.event.events.model.Event;
import com.event.events.model.Guest;
import com.event.events.model.SavedEvent;
import com.event.events.model.Vendor;
import com.event.events.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @GetMapping
    public ApiResponse<?> getAllGuests() {
        return ApiResponse.success(
                "Guests fetched successfully",
                guestService.getAllGuests()
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<Guest> getGuestById(
            @PathVariable String id
    ) {
        return ApiResponse.success(
                "Guest fetched successfully",
                guestService.getGuestById(id)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<Guest> updateGuestById(
            @PathVariable String id,
            @RequestBody Guest request
    ) {
        return ApiResponse.success(
                "Guest updated successfully",
                guestService.updateGuestById(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteGuestById(
            @PathVariable String id
    ) {
        guestService.deleteGuestById(id);

        return ApiResponse.success(
                "Guest deleted successfully",
                null
        );
    }

    @GetMapping("/vendors")
    public ApiResponse<?> getAllVendors(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int limit
    ) {
        return ApiResponse.success(
                "Vendors fetched successfully",
                guestService.getAllVendors(
                        category,
                        location,
                        search,
                        sortBy,
                        page,
                        limit
                )
        );
    }

    @GetMapping("/vendors/{vendorId}")
    public ApiResponse<Vendor> getVendorById(
            @PathVariable String vendorId
    ) {
        return ApiResponse.success(
                "Vendor fetched successfully",
                guestService.getVendorById(vendorId)
        );
    }

    @GetMapping("/profile/{id}")
    public ApiResponse<Guest> getGuestProfile(
            @PathVariable String id
    ) {
        return ApiResponse.success(
                "Profile fetched successfully",
                guestService.getGuestProfile(id)
        );
    }

    @PutMapping("/profile/{id}")
    public ApiResponse<Guest> updateGuestProfile(
            @PathVariable String id,
            @Valid @RequestBody UpdateGuestProfileRequest request
    ) {
        return ApiResponse.success(
                "Profile updated successfully",
                guestService.updateGuestProfile(id, request)
        );
    }

    @PutMapping("/password/{id}")
    public ApiResponse<?> updateGuestPassword(
            @PathVariable String id,
            @Valid @RequestBody UpdateGuestPasswordRequest request
    ) {
        guestService.updateGuestPassword(id, request);

        return ApiResponse.success(
                "Password updated successfully",
                null
        );
    }

    @GetMapping("/dashboard/{email}")
    public ApiResponse<?> getGuestDashboard(
            @PathVariable String email
    ) {
        return ApiResponse.success(
                "Dashboard fetched successfully",
                guestService.getGuestDashboard(email)
        );
    }

    @PutMapping("/ban/{id}")
    public ApiResponse<Guest> banOrSuspendGuest(
            @PathVariable String id,
            @RequestParam Boolean isBanned,
            @RequestParam(required = false) String banReason,
            @RequestParam(required = false) String bannedUntil
    ) {
        return ApiResponse.success(
                "Guest updated successfully",
                guestService.banOrSuspendGuest(
                        id,
                        isBanned,
                        banReason,
                        bannedUntil
                )
        );
    }

    @PostMapping("/booking-request/{vendorId}")
    public ApiResponse<Booking> sendBookingRequest(
            @PathVariable String vendorId,
            @Valid @RequestBody SendBookingRequest request
    ) {
        return ApiResponse.success(
                "Booking request sent successfully",
                guestService.sendBookingRequest(vendorId, request)
        );
    }

    @PostMapping("/save-event/{guestId}/{eventId}")
    public ApiResponse<SavedEvent> saveEvent(
            @PathVariable String guestId,
            @PathVariable String eventId
    ) {
        return ApiResponse.success(
                "Event saved successfully",
                guestService.saveEvent(guestId, eventId)
        );
    }

    @DeleteMapping("/unsave-event/{guestId}/{eventId}")
    public ApiResponse<?> unsaveEvent(
            @PathVariable String guestId,
            @PathVariable String eventId
    ) {
        guestService.unsaveEvent(guestId, eventId);

        return ApiResponse.success(
                "Event unsaved successfully",
                null
        );
    }

    @GetMapping("/saved-events/{guestId}")
    public ApiResponse<?> getSavedEvents(
            @PathVariable String guestId
    ) {
        return ApiResponse.success(
                "Saved events fetched successfully",
                guestService.getSavedEvents(guestId)
        );
    }

    @GetMapping("/saved/{guestId}/{eventId}")
    public ApiResponse<Boolean> isEventSaved(
            @PathVariable String guestId,
            @PathVariable String eventId
    ) {
        return ApiResponse.success(
                "Event save status fetched",
                guestService.isEventSaved(guestId, eventId)
        );
    }

    @GetMapping("/trending-events")
    public ApiResponse<?> getTrendingEvents() {
        return ApiResponse.success(
                "Trending events fetched successfully",
                guestService.getTrendingEvents()
        );
    }
}