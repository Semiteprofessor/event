package com.event.events.model;

import com.event.events.model.embeded.Activity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "events")
public class Event {

    @Id
    private String id;

    private String userId;

    private String name;
    private String slug;
    private String description;
    private String organizer;

    private String hostEmail;
    private String organizerEmail;
    private List<String> guests;

    private String address;
    private String city;
    private Integer pincode;

    private String date;
    private String startTime;
    private String stopTime;

    private List<String> media;
    private List<String> sideAttractions;

    private List<Activity> activities;

    private List<TicketType> ticketTypes;

    private boolean allowInstallment;

    private InstallmentConfig installmentConfig;

    private String posterEmail;
    private List<String> attendeesEmail;

    private List<String> hostWishes;

    private boolean isPrivate;

    private EventStatus status = EventStatus.PUBLISHED;

    private List<RegistrationType> registrationType;

    private List<Attendee> attendees;

    private List<String> wishlistItems;

    private Date createdAt;
    private Date updatedAt;
}
