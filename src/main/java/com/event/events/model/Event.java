package com.event.events.model;

import com.event.events.enums.EventStatus;
import com.event.events.model.embeded.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "events",
        indexes = {
                @Index(name = "idx_event_slug", columnList = "slug"),
                @Index(name = "idx_event_user", columnList = "userId"),
                @Index(name = "idx_event_status", columnList = "status")
        }
)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String organizer;

    private String hostEmail;

    private String organizerEmail;

    @ElementCollection
    @CollectionTable(
            name = "event_guests",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "guest_email")
    @Builder.Default
    private List<String> guests = new ArrayList<>();

    private String address;

    private String city;

    private Integer pincode;

    private String date;

    private String startTime;

    private String stopTime;

    @ElementCollection
    @CollectionTable(
            name = "event_media",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "media_url")
    @Builder.Default
    private List<String> media = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "event_side_attractions",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "attraction")
    @Builder.Default
    private List<String> sideAttractions = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "event_activities",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Builder.Default
    private List<Activity> activities = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "event_ticket_types",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Builder.Default
    private List<TicketType> ticketTypes = new ArrayList<>();

    @Builder.Default
    private boolean allowInstallment = false;

    @Embedded
    private InstallmentConfig installmentConfig;

    private String posterEmail;

    @ElementCollection
    @CollectionTable(
            name = "event_attendee_emails",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "attendee_email")
    @Builder.Default
    private List<String> attendeesEmail = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "event_host_wishes",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "wish")
    @Builder.Default
    private List<String> hostWishes = new ArrayList<>();

    @Builder.Default
    private boolean isPrivate = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventStatus status = EventStatus.PUBLISHED;

    @ElementCollection(targetClass = Notifications.RegistrationType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "event_registration_types",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "registration_type")
    @Builder.Default
    private List<Notifications.RegistrationType> registrationType = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Builder.Default
    private List<Attendee> attendees = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "event_wishlist_items",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "wishlist_item")
    @Builder.Default
    private List<String> wishlistItems = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}