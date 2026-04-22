package com.lineyk.characterchat.domain.chat.dto;

import com.lineyk.characterchat.domain.chat.entity.Sender;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessage(
        UUID chatRoomId,
        String message,
        Sender sender,
        LocalDateTime timestamp
) {
}
