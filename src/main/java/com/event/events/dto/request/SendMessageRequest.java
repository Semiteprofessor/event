package com.event.events.dto.request;

import lombok.Data;

@Data
public class SendMessageRequest {

    private String receiverId;

    private String message;
}