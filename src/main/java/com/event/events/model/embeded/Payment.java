package com.event.events.model.embeded;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Data
public class Payment {
    private BigDecimal amount;
    private Date date;
    private String reference;

    @CreationTimestamp
    private Instant createdAt;
}
