package com.lineyk.characterchat.domain.chat.controller;

import com.lineyk.characterchat.domain.chat.dto.ChatRoomCreateRequest;
import com.lineyk.characterchat.domain.chat.dto.ChatRoomResponse;
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
    public ResponseEntity<?> join(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ChatRoomResponse response = chatRoomService.getChatRoom(id, userDetails.user());
        return ResponseEntity.ok(response);
    }
}
