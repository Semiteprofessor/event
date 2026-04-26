package com.event.events.model.embeded;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class InstallmentDetails {

    private Integer numberOfInstallments = 1;

    private Double amountPerInstallment = 0.0;

    private Integer installmentsPaid = 0;

    private Double totalPaid = 0.0;

    private Double remainingAmount = 0.0;

    private List<Payment> payments;

    private Double amountPaid = 0.0;

    private Double balance = 0.0;

    private Date nextPaymentDate;
}
