package com.event.events.controller;

import com.event.events.service.StripeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/stripe")
    public String payWithStripe(@RequestParam Long amount) throws Exception {
        return stripeService.createCheckoutSession(
                "http://localhost:3000/success",
                "http://localhost:3000/cancel",
                amount
        );
    }
}
