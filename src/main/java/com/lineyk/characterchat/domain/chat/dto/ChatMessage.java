package com.lineyk.characterchat.domain.chat.dto;

import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.Sender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChatMessage(
        UUID chatId,
        UUID chatRoomId,
        String message,
        Sender sender,
        LocalDateTime timestamp,
        List<MessageSegment> segments
) {
    public static ChatMessage from(Chat chat) {
        return new ChatMessage(
                chat.getId(),
                chat.getChatRoom().getId(),
                chat.getMessage(),
                chat.getSenderType(),
                chat.getCreatedAt(),
                null
        );
    }

    public static ChatMessage fromAi(Chat chat, List<MessageSegment> segments) {
        return new ChatMessage(
                chat.getId(),
                chat.getChatRoom().getId(),
                chat.getMessage(),
                chat.getSenderType(),
                chat.getCreatedAt(),
                segments
        );
    }
}
