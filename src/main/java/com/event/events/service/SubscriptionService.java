package com.event.events.service;

import com.event.events.enums.SubscriptionStatus;
import com.event.events.exception.AuthException;
import com.event.events.model.PricingPlan;
import com.event.events.model.Subscription;
import com.event.events.model.User;
import com.event.events.repository.PricingPlanRepository;
import com.event.events.repository.SubscriptionRepository;
import com.event.events.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PricingPlanRepository pricingPlanRepository;

    @Transactional
    public Subscription activateSubscription(String userId, String planId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(404, "User not found"));

        PricingPlan plan = pricingPlanRepository.findById(planId)
                .orElseThrow(() -> new AuthException(404, "Plan not found"));

        Instant now = Instant.now();

        Instant endDate = calculateEndDate(plan.getBillingCycle(), now);

        Subscription subscription = Subscription.builder()
                .userId(userId)
                .planId(planId)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(now)
                .endDate(endDate)
                .paymentProvider(null)
                .paymentReference(null)
                .build();

        Subscription saved = subscriptionRepository.save(subscription);

        // Optional: update user plan info (if your User entity supports it)
        user.setUpdatedAt(now);
        userRepository.save(user);

        return saved;
    }

    private Instant calculateEndDate(Object billingCycle, Instant start) {

        String cycle = String.valueOf(billingCycle);

        return switch (cycle.toUpperCase()) {
            case "MONTHLY" -> start.plus(Duration.ofDays(30));
            case "YEARLY" -> start.plus(Duration.ofDays(365));
            case "WEEKLY" -> start.plus(Duration.ofDays(7));
            default -> throw new AuthException(400, "Invalid billing cycle");
        };
    }
}