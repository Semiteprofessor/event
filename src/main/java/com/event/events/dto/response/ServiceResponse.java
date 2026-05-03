package com.event.events.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceResponse<T> {

    private int status;

    private boolean success;

    private String message;

    private T data;

    private String error;
}