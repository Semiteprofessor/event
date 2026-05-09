package com.event.events.repository;

import com.event.events.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<User, String> {

    List<User> findByRole(String role);

    Optional<User> findByEmail(String email);
}