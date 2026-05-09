package com.lineyk.characterchat.global.ai.dto;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

public record AiMessage(
    Role role,
    String content
) {
    public enum Role {
        SYSTEM,
        USER,
        ASSISTANT
    }

    public Message toSpringAiMessage() {
        return switch (role) {
            case SYSTEM -> new SystemMessage(content);
            case USER -> new UserMessage(content);
            case ASSISTANT -> new AssistantMessage(content);
        };
    }
}
