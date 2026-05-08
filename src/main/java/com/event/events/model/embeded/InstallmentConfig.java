package com.event.events.model.embeded;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Embeddable
public class InstallmentConfig {

    private Integer numberOfInstallments = 1;

    private BigDecimal minPerInstallment = BigDecimal.ZERO;

    @ElementCollection
    private List<Instant> dueDates;
}