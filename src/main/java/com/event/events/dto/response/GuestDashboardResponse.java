package com.event.events.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuestDashboardResponse {

    private long bookedCount;

    private long hostedCount;

    private long savedCount;
}