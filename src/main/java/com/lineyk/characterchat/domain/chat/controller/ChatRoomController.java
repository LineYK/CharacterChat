package com.lineyk.characterchat.domain.chat.controller;

import com.lineyk.characterchat.domain.chat.dto.AiModelResponse;
import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.dto.ChatRoomCreateRequest;
import com.lineyk.characterchat.domain.chat.dto.ChatRoomResponse;
import com.lineyk.characterchat.domain.chat.service.ChatService;
import com.lineyk.characterchat.domain.chat.service.ChatRoomService;
import com.lineyk.characterchat.global.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {
    
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    
    @GetMapping("/models")
    public List<AiModelResponse> getAiModels() {
        return chatRoomService.getAvailableAiModels();
    }

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChatRoomCreateRequest request
            ) {
        ChatRoomResponse response = chatRoomService.createChatRoom(userDetails.user(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatRoomResponse> chatRooms = chatRoomService.getChatRooms(userDetails.user());
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChatRoom(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ChatRoomResponse response = chatRoomService.getChatRoom(id, userDetails.user());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatRoomService.deleteChatRoom(id, userDetails.user());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatMessage> messages = chatService.getChatMessages(id, userDetails.user());
        return ResponseEntity.ok(messages);
    }
}
