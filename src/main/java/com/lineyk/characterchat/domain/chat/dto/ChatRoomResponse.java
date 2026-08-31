package com.lineyk.characterchat.domain.chat.dto;

import com.lineyk.characterchat.domain.chat.entity.ChatRoom;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;


@Schema(description = "채팅방 응답 DTO")
public record ChatRoomResponse(
        UUID id,
        String characterName,
        String summaryMessage,
        boolean isCreated,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static ChatRoomResponse createFrom(ChatRoom chatRoom) {
        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getChatCharacter().getName(),
                chatRoom.getSummaryMessage(),
                true,
                chatRoom.getCreatedAt(),
                chatRoom.getUpdatedAt());
    }
    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getChatCharacter().getName(),
                chatRoom.getSummaryMessage(),
                false,
                chatRoom.getCreatedAt(),
                chatRoom.getUpdatedAt());
    }
}
