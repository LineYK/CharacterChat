package com.lineyk.characterchat.global.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lineyk.characterchat.domain.chat.dto.EmotionData;

public class EmotionTagParser {
    
    private static final Pattern EMOTION_PATTERN = Pattern.compile("\\[emotion:(\\w+):([\\d.]+)]");

    public static EmotionParsedResult parse(String message) {
        Matcher matcher = EMOTION_PATTERN.matcher(message);
        if (matcher.find()) {
            EmotionData emotion = new EmotionData(
                matcher.group(1), // 감정: happy, sad, angry, surprised, neutral, love
                Double.parseDouble(matcher.group(2)) // 강도: 0.0~1.0
            );
            String cleanedMessage = matcher.replaceAll("").trim();
            return new EmotionParsedResult(cleanedMessage, emotion);
        }
        return new EmotionParsedResult(message, null);
    }

    public record EmotionParsedResult(String message, EmotionData emotion) {}    

}
