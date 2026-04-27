package com.event.events.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaystackService {

    @Value("${paystack.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> initializePayment(String email, double amount) {

        String url = "https://api.paystack.co/transaction/initialize";

        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("amount", (int)(amount * 100)); // kobo

        return restTemplate.postForObject(url, body, Map.class);
    }
}
