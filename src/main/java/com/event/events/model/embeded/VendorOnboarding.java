package com.event.events.model.embeded;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Embeddable
public class VendorOnboarding {

    private String businessName;

    @Email
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$")
    private String phone;

    private String category;

    private String location;

    @ElementCollection
    private List<String> portfolioItems;

    private BigDecimal startingPrice;
}