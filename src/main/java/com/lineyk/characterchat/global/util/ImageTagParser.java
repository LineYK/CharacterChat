package com.lineyk.characterchat.global.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lineyk.characterchat.domain.chat.dto.MessageSegment;

public class ImageTagParser {
    
    private static final Pattern IMAGE_TAG_PATTERN = Pattern.compile("\\[img:([^\\]]+)]");

    public static List<MessageSegment> parse(String message, Map<String, String> tagToImageUrl) {

        List<MessageSegment> segments = new ArrayList<>();
        Matcher matcher = IMAGE_TAG_PATTERN.matcher(message);
        int lastIndex = 0;

        while (matcher.find()) {
            // 태그 앞의 텍스트
            if (matcher.start() > lastIndex) {
                String text = message.substring(lastIndex, matcher.start()).trim();
                if (!text.isEmpty()) {
                    segments.add(MessageSegment.ofText(text));
                }
            }

            // 태그에서 이미지 URL 추출
            String tag = matcher.group(1);
            String imageUrl = tagToImageUrl.get(tag);
            if (imageUrl != null) {
                segments.add(MessageSegment.ofImage(imageUrl, tag));
            }
            lastIndex = matcher.end();
        }

        if (lastIndex < message.length()) {
            String text = message.substring(lastIndex).trim();
            if (!text.isEmpty()) {
                segments.add(MessageSegment.ofText(text));
            }
        }

        return segments;
    }
}
