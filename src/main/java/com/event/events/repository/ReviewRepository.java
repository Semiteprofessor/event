package com.event.events.repository;

import com.event.events.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {

    List<Review> findByVendor(String vendorId);

}
