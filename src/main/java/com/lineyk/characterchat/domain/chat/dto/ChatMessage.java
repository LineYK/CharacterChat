package com.lineyk.characterchat.domain.chat.dto;

import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.Sender;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessage(
        UUID chatId,
        UUID chatRoomId,
        String message,
        Sender sender,
        LocalDateTime timestamp
) {
    public static ChatMessage from(Chat chat) {
        return new ChatMessage(
                chat.getId(),
                chat.getChatRoom().getId(),
                chat.getMessage(),
                chat.getSenderType(),
                chat.getCreatedAt()
        );
    }
}
