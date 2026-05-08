package com.event.events.repository;

import com.event.events.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, String> {
}