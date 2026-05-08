package com.event.events.repository;

import com.event.events.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderId(String providerId);

    Optional<User> findByResetToken(String token);
}