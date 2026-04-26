package com.event.events.model.embeded;

import lombok.Data;
import java.util.List;

@Data
public class VendorOnboarding {
    private String businessName;
    private String email;
    private String phone;
    private String category;
    private String location;
    private List<String> portfolioItems;
    private Double startingPrice;
}
