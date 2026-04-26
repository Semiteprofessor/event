package com.event.events.model.embeded;

import com.event.events.enums.AttendeeStatus;
import lombok.Data;

@Data
public class Attendee {

    private String userId;
    private String email;
    private AttendeeStatus status = AttendeeStatus.ABSENT;
}
