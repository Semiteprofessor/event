package com.event.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat_rooms")
public class ChatRoom {

    @Id
    private String id;

    @Indexed
    private List<String> participants;

    private String lastMessage;

    private String lastMessageSender;

    private Date createdAt;
    private Date updatedAt;
}
