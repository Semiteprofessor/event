package com.event.events.model;

import lombok.Data;
import java.util.Date;

@Data
public class Payment {
    private Double amount;
    private Date date;
    private String reference;
}
