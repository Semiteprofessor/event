package com.event.events.repository;

import com.event.events.model.PricingPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PricingPlanRepository extends MongoRepository<PricingPlan, String> {

    Optional<PricingPlan> findBySlug(String slug);

}
