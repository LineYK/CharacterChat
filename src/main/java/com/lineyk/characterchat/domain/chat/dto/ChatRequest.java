package com.lineyk.characterchat.domain.chat.dto;

import com.lineyk.characterchat.global.ai.constant.AiModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatRequest(
    @NotBlank(message = "메시지는 비어있을 수 없습니다.")
    String message,

    @NotNull(message = "AI 모델을 선택해주세요.")
    AiModel model
) {
    
}
