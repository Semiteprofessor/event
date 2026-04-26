package com.event.events.model.embeded;

import lombok.Data;

@Data
public class NotificationPreference {
    private boolean newBookingRequest = true;
    private boolean newMessages = true;
    private boolean reviewAlerts = true;
}
