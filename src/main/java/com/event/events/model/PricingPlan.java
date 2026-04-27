package com.event.events.model;

import com.event.events.enums.BillingCycle;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "pricing_plans")
public class PricingPlan {

    @Id
    private String id;

    @NotBlank
    private String plan;

    @NotBlank
    private String slug;

    @NotNull
    private Double price;

    private String tag;

    @NotEmpty
    private List<String> features;

    @NotNull
    private BillingCycle billingCycle;

    private Date createdAt;
    private Date updatedAt;
}