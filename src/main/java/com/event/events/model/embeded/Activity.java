package com.event.events.model.embeded;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class Activity {

    private String name;

    private String activityStartTime;

    private String activityStopTime;

    private String speaker;

    @ElementCollection
    private List<String> panelists;
}