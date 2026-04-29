package com.event.events.repository;

import com.event.events.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRepository extends MongoRepository<Otp, String> {

    Optional<Otp> findByEmailAndOtpAndOtpType(
            String email,
            String otp,
            String otpType
    );

    void deleteByEmail(String email);
}
