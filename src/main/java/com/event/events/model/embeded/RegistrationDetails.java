package com.event.events.model.embeded;

import lombok.Data;
import java.util.List;

@Data
public class RegistrationDetails {
    private List<CustomField> customFields;
}
