package com.event.events.model.embeded;

import com.event.events.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class BookingTicket {

    private String type;

    private Integer count;

    private BigDecimal price;

    private String qrSlug;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.ONE_OFF;

    private boolean installment;

    @Embedded
    private InstallmentDetails installmentDetails;
}