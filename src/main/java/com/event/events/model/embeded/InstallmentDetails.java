package com.event.events.model.embeded;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstallmentDetails {

    @Builder.Default
    private Integer numberOfInstallments = 1;

    @Builder.Default
    private BigDecimal amountPerInstallment = BigDecimal.ZERO;

    @Builder.Default
    private Integer installmentsPaid = 0;

    @Builder.Default
    private BigDecimal totalPaid = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal remainingAmount = BigDecimal.ZERO;

    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    private Instant nextPaymentDate;
}