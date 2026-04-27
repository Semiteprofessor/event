package com.event.events.repository;

import com.event.events.model.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {

    List<Portfolio> findByVendor(String vendorId);

    List<Portfolio> findByCategory(String category);
}
