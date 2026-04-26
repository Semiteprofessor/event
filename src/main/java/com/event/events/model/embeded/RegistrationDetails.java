package com.event.events.model.embeded;

import com.event.events.model.CustomField;
import lombok.Data;
import java.util.List;

@Data
public class RegistrationDetails {
    private List<CustomField> customFields;
}
