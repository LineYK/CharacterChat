package com.lineyk.characterchat.domain.chatcharacter.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lineyk.characterchat.domain.chatcharacter.entity.ChatCharacter;
import com.lineyk.characterchat.domain.user.entity.User;

public record ChatCharacterResponse(
        UUID id,
        String name,
        String description,
        CreatorResponse creator,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record CreatorResponse(
            UUID id,
            String email,
            String nickname
    ) {
        public static CreatorResponse from(User creator) {
            return new CreatorResponse(
                    creator.getId(),
                    creator.getEmail(),
                    creator.getNickname()
            );
        }
    }

    public static ChatCharacterResponse from(ChatCharacter chatCharacter) {
        return new ChatCharacterResponse(
                chatCharacter.getId(),
                chatCharacter.getName(),
                chatCharacter.getDescription(),
                CreatorResponse.from(chatCharacter.getCreator()),
                chatCharacter.getCreatedAt(),
                chatCharacter.getUpdateAt()
        );
    }
}
