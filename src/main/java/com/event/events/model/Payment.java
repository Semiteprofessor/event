package com.event.events.model;

import com.event.events.enums.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    private String userId;
    private String planId;

    private double amount;

    private String provider; // stripe / paystack
    private String reference;

    private PaymentStatus status;

    private Date createdAt;
}
