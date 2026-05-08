package com.event.events.repository;

import com.event.events.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {

    List<Message> findByChatRoomOrderByCreatedAtAsc(String chatRoomId);

    List<Message> findByReceiverAndReadFalse(String receiverId);

    List<Message> findBySenderAndReceiver(String senderId, String receiverId);
}
