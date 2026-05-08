package com.event.events.model.embeded;

import com.event.events.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Embeddable
@Data
public class TicketType {

    private String type;

    private String description;

    private BigDecimal price;

    private Integer quantity = 0;

    private Integer count = 0;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.ONE_OFF;

    private boolean isInstallment;

    @Embedded
    private InstallmentDetails installmentDetails;

    @ElementCollection
    private List<String> benefits;

    private Integer remaining = 0;

    private String pdfUrl;

    private String qrUrl;
}