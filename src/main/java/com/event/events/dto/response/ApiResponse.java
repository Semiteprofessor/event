package com.event.events.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private boolean status;
    private String message;
    private T data;

    public ApiResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}