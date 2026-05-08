package com.event.events.model.embeded;

import com.event.events.model.Payment;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Embeddable
public class InstallmentDetails {

    private Integer numberOfInstallments = 1;

    private BigDecimal amountPerInstallment = BigDecimal.ZERO;

    private Integer installmentsPaid = 0;

    private BigDecimal totalPaid = BigDecimal.ZERO;

    private BigDecimal remainingAmount = BigDecimal.ZERO;

    @OneToMany
    private List<Payment> payments;

    private BigDecimal amountPaid = BigDecimal.ZERO;

    private BigDecimal balance = BigDecimal.ZERO;

    private Instant nextPaymentDate;
}