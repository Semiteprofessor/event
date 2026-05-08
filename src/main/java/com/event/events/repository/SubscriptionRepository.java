package com.event.events.repository;

import com.event.events.model.Subscription;
import com.event.events.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    Optional<Subscription> findByUserIdAndStatus(
            String userId,
            SubscriptionStatus status
    );

    List<Subscription> findByUserId(String userId);

    List<Subscription> findByPlanId(String planId);

    Optional<Subscription> findByUserIdAndStatusIn(
            String userId,
            List<SubscriptionStatus> statuses
    );
}