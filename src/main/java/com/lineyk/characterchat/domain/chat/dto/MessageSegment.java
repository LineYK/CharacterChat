package com.lineyk.characterchat.domain.chat.dto;

public record MessageSegment(
    String type,
    String content,
    String emotionTag
) {
    public static MessageSegment ofText(String content) {
        return new MessageSegment("text", content, null);
    }

    public static MessageSegment ofImage(String imageUrl, String emotionTag) {
        return new MessageSegment("image", imageUrl, emotionTag);
    }
}