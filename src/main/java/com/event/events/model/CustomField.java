package com.event.events.model;

import com.event.events.enums.FieldType;
import lombok.Data;
import java.util.List;

@Data
public class CustomField {
    private String label;
    private FieldType type;
    private boolean required = false;
    private List<String> options;
}
