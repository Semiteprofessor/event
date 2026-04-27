package com.event.events.model;

import com.event.events.enums.OtpType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "otps")
@CompoundIndex(name = "email_type_created_idx", def = "{'email':1, 'otpType':1, 'createdAt':-1}")
public class Otp {

    @Id
    private String id;

    @NotBlank
    private String otp;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    private OtpType otpType = OtpType.REGISTRATION;

    private Date createdAt;
    private Date updatedAt;
}
