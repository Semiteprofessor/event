package com.event.events.model;

import com.event.events.enums.VendorStatus;
import com.event.events.model.embeded.NotificationPreference;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "vendors")
public class Vendor {

    @Id
    private String id;

    @Indexed
    @NotBlank
    private String user;

    @NotBlank
    private String businessName;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$")
    private String phone;

    @NotBlank
    private String category;

    @NotBlank
    private String location;

    private String bio;
    private String avatar;

    private NotificationPreference notificationPreference = new NotificationPreference();

    private List<String> portfolioItems;

    @Min(0)
    private Double startingPrice;

    private VendorStatus status = VendorStatus.PENDING;

    private Date completedAt;

    private Date createdAt;
    private Date updatedAt;
}
