package com.event.events.repository;

import com.event.events.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, String> {

    List<Wishlist> findByEvent(String event);

    List<Wishlist> findByUserEmail(String userEmail);

    Optional<Wishlist> findByEventAndName(
            String event,
            String name
    );

    List<Wishlist> findByNameContainingIgnoreCase(String name);

    List<Wishlist> findByBrandContainingIgnoreCase(String brand);
}