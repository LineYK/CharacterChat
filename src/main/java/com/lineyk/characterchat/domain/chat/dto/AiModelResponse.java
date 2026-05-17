package com.lineyk.characterchat.domain.chat.dto;

import com.lineyk.characterchat.global.ai.constant.AiModel;

public record AiModelResponse(
    String key,
    String model,
    String provider
) {
    public static AiModelResponse from(AiModel aiModel) {
        return new AiModelResponse(
            aiModel.name(), 
            aiModel.getModel(), 
            aiModel.getProvider().name()
        );
    }
}
