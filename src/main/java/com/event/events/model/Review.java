package com.event.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @NotBlank
    private String vendor;

    @NotBlank
    private String reviewerName;

    @Email
    @NotBlank
    private String reviewerEmail;

    private String reviewerAvatar;

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private String comment;

    private String eventName;

    private Date createdAt;
    private Date updatedAt;
}