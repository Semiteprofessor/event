package com.event.events.model.embeded;

import lombok.Data;
import java.util.List;

@Data
public class Activity {
    private String name;
    private String activityStartTime;
    private String activityStopTime;
    private String speaker;
    private List<String> panelists;
}
