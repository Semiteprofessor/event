package com.event.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    @NotBlank
    private String chatRoom;

    @NotBlank
    private String sender;

    @NotBlank
    private String receiver;

    @NotBlank
    private String message;

    private boolean read = false;

    private Date createdAt;
    private Date updatedAt;
}
