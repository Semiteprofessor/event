package com.event.events.repository;

import com.event.events.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByUser(String userId);

    List<Notification> findByUserAndReadFalse(String userId);
}
