package com.event.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "wishlist_support")
public class WishlistSupport {

    @Id
    private String id;

    @NotBlank
    private String wishlist;

    @NotBlank
    private String supporter;

    @Min(1)
    private double amount;

    private String supporterName;

    private Date createdAt;
    private Date updatedAt;
}
