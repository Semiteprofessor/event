package com.event.events.dto;

import com.event.events.enums.BillingCycle;
import lombok.Data;
import java.util.List;

@Data
public class PricingPlanDTO {
    private String plan;
    private String slug;
    private Double price;
    private String tag;
    private List<String> features;
    private BillingCycle billingCycle;
}
