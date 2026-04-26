package com.event.events.model.embeded;

import lombok.Data;

@Data
public class Attendee {

    private String userId;
    private String email;
    private AttendeeStatus status = AttendeeStatus.ABSENT;
}
