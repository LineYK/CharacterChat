package com.lineyk.characterchat.domain.chat.dto;

import com.lineyk.characterchat.domain.chat.entity.ChatRoom;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatRoomResponse(
        UUID id,
        String characterName,
        String summaryMessage,
        LocalDateTime createdAt
) {
    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getChatCharacter().getName(),
                chatRoom.getSummaryMessage(),
                chatRoom.getCreatedAt()
        );
    }
}
