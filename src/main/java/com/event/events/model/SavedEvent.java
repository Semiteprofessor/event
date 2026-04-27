package com.event.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "saved_events")
public class SavedEvent {

    @Id
    private String id;

    private String guest;

    private String event;

    private Date createdAt;
    private Date updatedAt;
}
