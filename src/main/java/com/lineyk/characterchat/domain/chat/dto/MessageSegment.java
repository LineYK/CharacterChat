package com.lineyk.characterchat.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메시지 세그먼트 DTO")
public record MessageSegment(
    @Schema(description = "세그먼트 유형")
    String type,
    String content,
    @Schema(description = "감정 태그")
    String emotionTag
) {
    public static MessageSegment ofText(String content) {
        return new MessageSegment("text", content, null);
    }

    public static MessageSegment ofImage(String imageUrl, String emotionTag) {
        return new MessageSegment("image", imageUrl, emotionTag);
    }
}