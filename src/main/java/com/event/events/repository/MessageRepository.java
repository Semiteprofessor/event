package com.event.events.repository;

import com.event.events.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findByChatRoomOrderByCreatedAtAsc(String chatRoomId);

    List<Message> findByReceiverAndReadFalse(String receiverId);

    List<Message> findBySenderAndReceiver(String senderId, String receiverId);
}
