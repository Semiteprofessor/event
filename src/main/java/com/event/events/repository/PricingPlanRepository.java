package com.event.events.repository;

import com.event.events.model.PricingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingPlanRepository extends JpaRepository<PricingPlan, String> {

    Optional<PricingPlan> findBySlug(String slug);
}