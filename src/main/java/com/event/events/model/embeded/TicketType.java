package com.event.events.model.embeded;

import com.event.events.enums.PaymentType;
import lombok.Data;

import java.util.List;

@Data
public class TicketType {

    private String type;
    private String description;
    private Double price;
    private Integer quantity = 0;
    private Integer count = 0;
    private Double totalAmount = 0.0;

    private PaymentType paymentType = PaymentType.ONE_OFF;

    private boolean isInstallment;

    private InstallmentDetails installmentDetails;

    private List<String> benefits;

    private Integer remaining = 0;

    private String pdfUrl;
    private String qrUrl;
}
