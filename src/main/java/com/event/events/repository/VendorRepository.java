package com.event.events.repository;

import com.event.events.enums.VendorStatus;
import com.event.events.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface VendorRepository extends MongoRepository<Vendor, String> {

    List<Vendor> findByUser(String user);

    List<Vendor> findByStatus(VendorStatus status);
}
