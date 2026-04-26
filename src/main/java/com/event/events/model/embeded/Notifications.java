package com.event.events.model.embeded;

import lombok.Data;

@Data
public class Notifications {
    private boolean eventReminders = true;
    private boolean newMessages = true;
}
