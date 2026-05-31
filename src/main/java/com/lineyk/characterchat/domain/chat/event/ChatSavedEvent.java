package com.lineyk.characterchat.domain.chat.event;

import java.util.UUID;

import com.lineyk.characterchat.global.ai.constant.AiModel;

public record ChatSavedEvent(
    UUID chatId,
    UUID chatRoomId,
    UUID userId,
    AiModel aiModel
) {
    
}
