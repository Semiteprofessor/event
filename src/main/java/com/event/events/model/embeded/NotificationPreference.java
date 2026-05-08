package com.event.events.model.embeded;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class NotificationPreference {

    private boolean newBookingRequest = true;

    private boolean newMessages = true;

    private boolean reviewAlerts = true;
}