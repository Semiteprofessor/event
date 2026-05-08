package com.event.events.model.embeded;

import com.event.events.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
public class BookingTicket {

    private String type;

    private Integer count;

    private Double price;

    private String qrlSlug;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.ONE_OFF;

    private boolean isInstallment = false;

    @Embedded
    private InstallmentDetails installmentDetails;
}