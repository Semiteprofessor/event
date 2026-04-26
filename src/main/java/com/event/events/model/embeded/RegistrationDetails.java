package com.event.events.model.embeded;

import com.event.events.enums.FieldType;
import lombok.Data;
import java.util.List;

@Data
public class RegistrationDetails {
    private List<CustomField> customFields;

    @Data
    public static class CustomField {
        private String label;
        private FieldType type;
        private boolean required = false;
        private List<String> options;
    }
}
