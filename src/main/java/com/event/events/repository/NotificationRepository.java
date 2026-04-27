package com.event.events.repository;

import com.event.events.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByUser(String userId);

    List<Notification> findByUserAndReadFalse(String userId);
}
