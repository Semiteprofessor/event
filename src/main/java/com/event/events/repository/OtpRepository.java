package com.event.events.repository;

import com.event.events.enums.OtpType;
import com.event.events.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRepository extends MongoRepository<Otp, String> {

    Optional<Otp> findTopByEmailAndOtpTypeOrderByUpdatedAtDesc(
            String email,
            OtpType otpType
    );

    Optional<Otp> findByEmailAndOtpAndOtpType(
            String email,
            String otp,
            OtpType otpType
    );

    void deleteByEmailAndOtpType(String email, OtpType otpType);
}
