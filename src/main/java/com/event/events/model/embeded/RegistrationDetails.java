package com.event.events.model.embeded;

import com.event.events.enums.FieldType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class RegistrationDetails {

    @ElementCollection
    private List<CustomField> customFields;

    @Data
    @Embeddable
    public static class CustomField {

        private String label;

        @Enumerated(EnumType.STRING)
        private FieldType type;

        private boolean required = false;

        @ElementCollection
        private List<String> options;
    }
}