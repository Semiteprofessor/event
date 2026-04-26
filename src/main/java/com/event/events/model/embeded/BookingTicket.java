package com.event.events.model.embeded;

import lombok.Data;

@Data
public class BookingTicket {

    private String type;
    private Integer count;
    private Double price;
    private String qrlSlug;

    private Double totalAmount;

    private PaymentType paymentType = PaymentType.ONE_OFF;

    private boolean isInstallment = false;

    private InstallmentDetails installmentDetails;
}
