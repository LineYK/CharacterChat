package com.lineyk.characterchat.domain.chat.controller;

import com.lineyk.characterchat.application.chat.ChatFacade;
import com.lineyk.characterchat.domain.chat.dto.AffinityData;
import com.lineyk.characterchat.domain.chat.dto.AiModelResponse;
import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.dto.ChatRoomCreateRequest;
import com.lineyk.characterchat.domain.chat.dto.ChatRoomResponse;
import com.lineyk.characterchat.domain.chat.service.ChatRoomService;
import com.lineyk.characterchat.global.auth.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    public ResponseEntity<?> findOrCreate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChatRoomCreateRequest request
            ) {
        ChatRoomResponse response = chatRoomService.findOrCreateChatRoom(userDetails.user(), request);
        return response.isCreated() ? ResponseEntity.status(HttpStatus.CREATED).body(response) : ResponseEntity.ok(response);
    }

    @Operation(responses = {
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ChatRoomResponse.class)))
    })
    @GetMapping
    public ResponseEntity<?> getChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatRoomResponse> chatRooms = chatRoomService.getChatRooms(userDetails.user());
        return ResponseEntity.ok(chatRooms);
    }

    @Operation(responses = {
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ChatRoomResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getChatRoom(@PathVariable("id") UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ChatRoomResponse response = chatRoomService.getChatRoom(id, userDetails.user());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatRoomService.deleteChatRoom(id, userDetails.user());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable("id") UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatMessage> messages = chatFacade.getChatMessages(id, userDetails.user());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}/affinity")
    public ResponseEntity<?> getAffinity(
        @PathVariable("id") UUID id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        AffinityData affinityData = chatFacade.getAffinity(id, userDetails.user());
        return ResponseEntity.ok(affinityData);
    }
    
    @PostMapping("/{id}/dating/start")
    public ResponseEntity<?> startDating(
        @PathVariable("id") UUID id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        chatFacade.startDating(id, userDetails.user());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/dating/end")
    public ResponseEntity<?> endDating(
        @PathVariable("id") UUID id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        chatFacade.endDating(id, userDetails.user());
        return ResponseEntity.ok().build();
    }
    
}
