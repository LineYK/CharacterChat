package com.lineyk.characterchat.domain.chat.controller;

import com.lineyk.characterchat.application.chat.ChatFacade;
import com.lineyk.characterchat.domain.chat.dto.AffinityData;
import com.lineyk.characterchat.domain.chat.dto.AiModelResponse;
import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
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
    private final ChatFacade chatFacade;
    
    @GetMapping("/models")
    public List<AiModelResponse> getAiModels() {
        return chatRoomService.getAvailableAiModels();
    }

    @PostMapping
    public ResponseEntity<ChatRoomResponse> findOrCreate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChatRoomCreateRequest request
            ) {
        ChatRoomResponse response = chatRoomService.findOrCreateChatRoom(userDetails.user(), request);
        return response.isCreated() ? ResponseEntity.status(HttpStatus.CREATED).body(response) : ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatRoomResponse> chatRooms = chatRoomService.getChatRooms(userDetails.user());
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(@PathVariable("id") UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ChatRoomResponse response = chatRoomService.getChatRoom(id, userDetails.user());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatRoomService.deleteChatRoom(id, userDetails.user());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable("id") UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatMessage> messages = chatFacade.getChatMessages(id, userDetails.user());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}/affinity")
    public ResponseEntity<AffinityData> getAffinity(
        @PathVariable("id") UUID id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AffinityData affinityData = chatFacade.getAffinity(id, userDetails.user());
        return ResponseEntity.ok(affinityData);
    }
    
    @PostMapping("/{id}/dating/start")
    public ResponseEntity<Void> startDating(
        @PathVariable("id") UUID id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        chatFacade.startDating(id, userDetails.user());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/dating/end")
    public ResponseEntity<Void> endDating(
        @PathVariable("id") UUID id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        chatFacade.endDating(id, userDetails.user());
        return ResponseEntity.ok().build();
    }
    
}
