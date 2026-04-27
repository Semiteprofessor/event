package com.event.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "portfolios")
public class Portfolio {

    @Id
    private String id;

    @NotBlank
    private String vendor; // ObjectId → String (User reference)

    @NotBlank
    private String title;

    @NotBlank
    private String category;

    @NotBlank
    private String description;

    @NotBlank
    private String mediaUrl;

    @NotNull
    private MediaType mediaType = MediaType.IMAGE;

    private Date createdAt;
    private Date updatedAt;
}
