package com.event.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "wishlists")
public class Wishlist {

    @Id
    private String id;

    @NotBlank
    private String event;

    @Email
    private String userEmail;

    @NotBlank
    private String name;

    private String brand;

    private Integer quantity;

    private String size;

    private Double price;

    private String description;

    private List<String> images = List.of();

    private Double amountRaised;

    private Date createdAt;
    private Date updatedAt;
}
