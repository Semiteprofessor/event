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

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer count;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    private String qrSlug;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Embedded
    private InstallmentDetails installmentDetails;
}