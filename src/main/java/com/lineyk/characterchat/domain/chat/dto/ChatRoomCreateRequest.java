package com.lineyk.characterchat.domain.chat.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChatRoomCreateRequest(
        @NotNull UUID characterId
        ) {
}
