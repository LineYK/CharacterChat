package com.lineyk.characterchat.domain.chat.dto;

import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.ChatMode;
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
        List<MessageSegment> segments,
        ChatMode mode,
        EmotionData emotion,
        AffinityData affinity
) {
    public static ChatMessage from(Chat chat) {
        return new ChatMessage(
                chat.getId(),
                chat.getChatRoom().getId(),
                chat.getMessage(),
                chat.getSenderType(),
                chat.getCreatedAt(),
                null,
                chat.getMode(),
                null,
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
                segments,
                chat.getMode(),
                null,
                null
        );
    }

    public static ChatMessage fromAi(Chat chat, List<MessageSegment> segments, AffinityData affinity) {
        return new ChatMessage(
                chat.getId(),
                chat.getChatRoom().getId(),
                chat.getMessage(),
                chat.getSenderType(),
                chat.getCreatedAt(),
                segments,
                chat.getMode(),
                null,
                affinity
        );
    }

    public static ChatMessage fromAi(Chat chat, List<MessageSegment> segments, EmotionData emotion, AffinityData affinity) {
        return new ChatMessage(
                chat.getId(),
                chat.getChatRoom().getId(),
                chat.getMessage(),
                chat.getSenderType(),
                chat.getCreatedAt(),
                segments,
                chat.getMode(),
                emotion,
                affinity
        );
    }
}
