package com.event.events.model;

import com.event.events.enums.SubscriptionStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subscriptions")
public class Subscription {

    @Id
    private String id;

    private String userId;
    private String planId;

    private SubscriptionStatus status;

    private Date startDate;
    private Date endDate;

    private String paymentProvider; // stripe / paystack
    private String paymentReference;
}
