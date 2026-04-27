package com.event.events.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    @PostMapping("/stripe")
    public void handleStripeWebhook(@RequestBody String payload) {
        // verify signature
        // parse event
        // update payment + subscription
    }
}
