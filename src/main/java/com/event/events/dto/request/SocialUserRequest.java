package com.event.events.dto.request;

import lombok.Data;

@Data
public class SocialUserRequest {
    private String email;
    private String name;
    private String provider;
}
