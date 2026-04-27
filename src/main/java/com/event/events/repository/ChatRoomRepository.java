package com.event.events.repository;

import com.event.events.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    List<ChatRoom> findByParticipantsContaining(String userId);

    Optional<ChatRoom> findByParticipantsContainingAndParticipantsContaining(
            String user1,
            String user2
    );
}
