package com.event.events.repository;

import com.event.events.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByVendor(String vendorId);

}
