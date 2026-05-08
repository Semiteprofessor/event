package com.event.events.model.embeded;

import com.event.events.enums.RegistrationMode;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Notifications {

    @Builder.Default
    private boolean eventReminders = true;

    @Builder.Default
    private boolean newMessages = true;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class RegistrationType {

        @Enumerated(EnumType.STRING)
        private RegistrationMode type;

        @Embedded
        private RegistrationDetails details;
    }
}