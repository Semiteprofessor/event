package com.event.events.repository;

import com.event.events.model.WishlistSupport;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface WishlistSupportRepository extends MongoRepository<WishlistSupport, String> {

    List<WishlistSupport> findByWishlist(String wishlistId);

    List<WishlistSupport> findBySupporter(String supporterId);
}
