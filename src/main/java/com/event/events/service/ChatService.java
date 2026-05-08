package com.event.events.service;

import com.event.events.dto.request.SendMessageRequest;
import com.event.events.dto.request.StartChatRequest;
import com.event.events.exception.AuthException;
import com.event.events.model.ChatRoom;
import com.event.events.model.Message;
import com.event.events.repository.ChatRoomRepository;
import com.event.events.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public ChatRoom startChat(
            String senderId,
            StartChatRequest request
    ) {

        if (senderId.equals(request.getReceiverId())) {
            throw new AuthException(
                    400,
                    "Cannot start chat with yourself"
            );
        }

        return getOrCreateRoom(
                senderId,
                request.getReceiverId()
        );
    }

    public List<ChatRoom> getMyChats(String userId) {

        return chatRoomRepository
                .findByParticipantsContaining(userId);
    }

    public List<Message> getMessages(String roomId) {

        return messageRepository
                .findByChatRoomOrderByCreatedAtAsc(roomId);
    }

    @Transactional
    public Message sendMessage(
            String senderId,
            SendMessageRequest request
    ) {

        ChatRoom room = getOrCreateRoom(
                senderId,
                request.getReceiverId()
        );

        Message message = Message.builder()
                .chatRoom(room.getId())
                .sender(senderId)
                .receiver(request.getReceiverId())
                .message(request.getMessage())
                .createdAt(Instant.now())
                .build();

        Message saved = messageRepository.save(message);

        room.setLastMessage(request.getMessage());
        room.setUpdatedAt(Instant.now());

        chatRoomRepository.save(room);

        return saved;
    }

    private ChatRoom getOrCreateRoom(
            String senderId,
            String receiverId
    ) {

        return chatRoomRepository
                .findByParticipantsContainingAndParticipantsContaining(
                        senderId,
                        receiverId
                )
                .orElseGet(() -> {

                    ChatRoom room = ChatRoom.builder()
                            .participants(
                                    List.of(senderId, receiverId)
                            )
                            .createdAt(Instant.now())
                            .updatedAt(Instant.now())
                            .build();

                    return chatRoomRepository.save(room);
                });
    }
}