package com.lineyk.characterchat.global.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lineyk.characterchat.domain.chat.dto.EmotionData;

public class EmotionTagParser {
    
    private static final Pattern EMOTION_PATTERN = Pattern.compile("\\[emotion:(\\w+):([\\d.]+)]");

    public static EmotionData parse(String message) {
        Matcher matcher = EMOTION_PATTERN.matcher(message);
        if (matcher.find()) {
            String cleanedMessage = matcher.replaceAll("").trim();
            EmotionData emotion = new EmotionData(
                matcher.group(1), // 감정: happy, sad, angry, surprised, neutral, love
                Double.parseDouble(matcher.group(2)),
                cleanedMessage
            );
            return emotion;
        }
        return new EmotionData(null, 0.0, message); // 감정 태그가 없으면 기본값 반환
    }
}
