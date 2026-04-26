package com.event.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "wishlist_support")
public class WishlistSupport {

    @Id
    private String id;

    private String wishlist;
    private Double amount;
}
