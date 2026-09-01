package com.lineyk.characterchat.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감정 데이터 DTO")
public record EmotionData(
    String tag,
    double intensity,
    String message
) {
    
}
