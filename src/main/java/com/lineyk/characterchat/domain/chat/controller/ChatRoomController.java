package com.lineyk.characterchat.domain.chat.controller;

import com.lineyk.characterchat.domain.chat.dto.ChatRoomCreateRequest;
import com.lineyk.characterchat.domain.chat.dto.ChatRoomResponse;
import com.lineyk.characterchat.domain.chat.service.ChatRoomService;
import com.lineyk.characterchat.global.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChatRoomCreateRequest request
            ) {
        ChatRoomResponse response = chatRoomService.createChatRoom(userDetails.user(), request);
        return ResponseEntity.ok(response);
    }
}
