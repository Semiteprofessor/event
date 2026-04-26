package com.event.events.model.embeded;

import com.event.events.enums.RegistrationMode;
import lombok.Data;

@Data
public class Notifications {
    private boolean eventReminders = true;
    private boolean newMessages = true;

    @Data
    public static class RegistrationType {

        private RegistrationMode type;
        private RegistrationDetails details;
    }
}
