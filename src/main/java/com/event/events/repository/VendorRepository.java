package com.event.events.repository;

import com.event.events.enums.VendorStatus;
import com.event.events.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, String> {

    List<Vendor> findByUser(String user);

    List<Vendor> findByStatus(VendorStatus status);
}
