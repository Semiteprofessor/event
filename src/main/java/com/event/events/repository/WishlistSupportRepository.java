package com.event.events.repository;

import com.event.events.model.WishlistSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistSupportRepository extends JpaRepository<WishlistSupport, String> {

    List<WishlistSupport> findByWishlist(String wishlistId);

    List<WishlistSupport> findBySupporter(String supporterId);
}
