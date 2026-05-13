package com.lineyk.characterchat.global.ai.strategy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Component;

import com.lineyk.characterchat.global.ai.constant.AiProvider;
import com.lineyk.characterchat.global.ai.dto.AiMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GroqModelStrategy implements AiModelStrategy {

    private final OpenAiChatModel chatModel;
    
    @Override
    public AiProvider provider() {
        return AiProvider.GROQ;
    }

    @Override
    public String chat(String model, String systemPrompt, List<AiMessage> history, String userMessage) {
        List<Message> aiMessages = new ArrayList<>();
        aiMessages.add(new SystemMessage(systemPrompt));
        
        history.stream()
            .map(AiMessage::toSpringAiMessage)
            .forEach(aiMessages::add);
        
        aiMessages.add(new UserMessage(userMessage));

        OpenAiChatOptions options = OpenAiChatOptions.builder()
            .model(model)
            .build();
        
        return chatModel.call(new Prompt(aiMessages, options))
            .getResult().getOutput().getText();
    }

}
