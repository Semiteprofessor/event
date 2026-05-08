package com.event.events.model.embeded;

import com.event.events.enums.AttendeeStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Attendee {

    private String userId;

    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AttendeeStatus status = AttendeeStatus.ABSENT;
}