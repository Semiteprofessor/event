package com.event.events.repository;

import com.event.events.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {

    List<Portfolio> findByVendor(String vendorId);

    List<Portfolio> findByCategory(String category);
}
