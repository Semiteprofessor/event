package com.event.events.service;

import com.event.events.dto.request.BookingVendorRequest;
import com.event.events.dto.request.UpdateGuestPasswordRequest;
import com.event.events.dto.request.UpdateGuestProfileRequest;
import com.event.events.dto.response.GuestDashboardResponse;
import com.event.events.enums.BookingStatus;
import com.event.events.enums.Role;
import com.event.events.exception.AuthException;
import com.event.events.model.Booking;
import com.event.events.model.Event;
import com.event.events.model.User;
import com.event.events.repository.BookingRepository;
import com.event.events.repository.EventRepository;
import com.event.events.repository.GuestRepository;
import com.event.events.repository.SavedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final SavedEventRepository savedEventRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllGuests() {

        return guestRepository.findByRole(
                Role.GUEST.name()
        );
    }

    public User getGuestById(String id) {

        return guestRepository.findById(id)
                .orElseThrow(() ->
                        new AuthException(
                                404,
                                "Guest not found"
                        ));
    }

    public User updateGuestById(
            String id,
            UpdateGuestProfileRequest request
    ) {

        User guest = getGuestById(id);

        guest.setName(request.getName());
        guest.setPhone(request.getPhone());
        guest.setAddress(request.getAddress());
        guest.setProfileImage(request.getProfileImage());

        return guestRepository.save(guest);
    }

    public void deleteGuest(String id) {

        User guest = getGuestById(id);

        guestRepository.delete(guest);
    }

    public User getGuestProfile(String guestId) {

        return getGuestById(guestId);
    }

    public User updateGuestProfile(
            String guestId,
            UpdateGuestProfileRequest request
    ) {

        User guest = getGuestById(guestId);

        guest.setName(request.getName());
        guest.setPhone(request.getPhone());
        guest.setAddress(request.getAddress());

        return guestRepository.save(guest);
    }

    public void updateGuestPassword(
            String guestId,
            UpdateGuestPasswordRequest request
    ) {

        User guest = getGuestById(guestId);

        boolean matches = passwordEncoder.matches(
                request.getCurrentPassword(),
                guest.getPassword()
        );

        if (!matches) {
            throw new AuthException(
                    400,
                    "Incorrect password"
            );
        }

        guest.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        guestRepository.save(guest);
    }

    public GuestDashboardResponse getDashboard(
            String guestId,
            String email
    ) {

        long bookedCount =
                bookingRepository.countByUserEmail(email);

        long savedCount =
                savedEventRepository.countByGuestId(guestId);

        return GuestDashboardResponse.builder()
                .bookedCount(bookedCount)
                .hostedCount(0)
                .savedCount(savedCount)
                .build();
    }

    public User banOrSuspendGuest(
            String guestId,
            boolean isBanned,
            String reason,
            Instant bannedUntil
    ) {

        User guest = getGuestById(guestId);

        guest.setBanned(isBanned);
        guest.setBanReason(reason);
        guest.setBannedUntil(bannedUntil);

        return guestRepository.save(guest);
    }

    public Booking sendBookingRequest(
            String guestId,
            String guestEmail,
            String vendorId,
            BookingVendorRequest request
    ) {

        Event event = Event.builder()
                .name(request.getEventName())
                .description(request.getMessage())
                .address(request.getEventLocation())
                .budget(request.getPriceOffered())
                .createdAt(Instant.now())
                .createdBy(guestId)
                .posterEmail(guestEmail)
                .startTime(
                        LocalDateTime.of(
                                request.getEventDate(),
                                request.getEventTime()
                        )
                )
                .stopTime(
                        LocalDateTime.of(
                                request.getEventDate(),
                                request.getEventTime()
                        ).plusHours(4)
                )
                .build();

        Event savedEvent =
                eventRepository.save(event);

        Booking booking = Booking.builder()
                .eventId(savedEvent.getId())
                .vendorId(vendorId)
                .guestId(guestId)
                .userEmail(guestEmail)
                .totalAmount(request.getPriceOffered())
                .message(request.getMessage())
                .status(BookingStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        return bookingRepository.save(booking);
    }
}