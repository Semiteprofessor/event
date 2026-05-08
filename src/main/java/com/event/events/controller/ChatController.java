package com.event.events.controller;

import com.event.events.dto.request.SendMessageRequest;
import com.event.events.dto.request.StartChatRequest;
import com.event.events.dto.response.ApiResponse;
import com.event.events.model.User;
import com.event.events.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/start")
    public ResponseEntity<?> startChat(
            @AuthenticationPrincipal User user,
            @RequestBody StartChatRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Chat room ready",
                        chatService.startChat(user.getId(), request)
                )
        );
    }

    @GetMapping("/my-chats")
    public ResponseEntity<?> getMyChats(
            @AuthenticationPrincipal User user
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Chats retrieved successfully",
                        chatService.getMyChats(user.getId())
                )
        );
    }

    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<?> getChatMessages(
            @PathVariable String chatRoomId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Messages retrieved successfully",
                        chatService.getMessages(chatRoomId)
                )
        );
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @AuthenticationPrincipal User user,
            @RequestBody SendMessageRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Message sent successfully",
                        chatService.sendMessage(user.getId(), request)
                )
        );
    }
}