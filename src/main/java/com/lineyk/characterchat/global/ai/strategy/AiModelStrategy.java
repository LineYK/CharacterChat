package com.lineyk.characterchat.global.ai.strategy;

import java.util.List;

import com.lineyk.characterchat.global.ai.constant.AiProvider;
import com.lineyk.characterchat.global.ai.dto.AiMessage;

public interface AiModelStrategy {
    AiProvider provider();
    String chat(String model, String systemPrompt, List<AiMessage> history, String userMessage);
}