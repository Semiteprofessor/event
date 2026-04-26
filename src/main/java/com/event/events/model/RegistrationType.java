package com.event.events.model;

import com.event.events.enums.RegistrationMode;
import com.event.events.model.embeded.RegistrationDetails;
import lombok.Data;

@Data
public class RegistrationType {

    private RegistrationMode type;
    private RegistrationDetails details;
}
