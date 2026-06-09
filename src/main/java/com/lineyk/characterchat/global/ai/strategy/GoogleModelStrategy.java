package com.lineyk.characterchat.global.ai.strategy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.stereotype.Component;

import com.lineyk.characterchat.global.ai.constant.AiProvider;
import com.lineyk.characterchat.global.ai.dto.AiMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleModelStrategy implements AiModelStrategy {
    
    private final GoogleGenAiChatModel chatModel;
    
    @Override
    public AiProvider provider() {
        return AiProvider.GOOGLE;
    }

    @Override
    public String chat(String model, String systemPrompt, List<AiMessage> messages) {

        List<Message> aiMessages = new ArrayList<>();
        aiMessages.add(new SystemMessage(systemPrompt));

        messages.stream()
            .map(AiMessage::toSpringAiMessage)
            .forEach(aiMessages::add);

        GoogleGenAiChatOptions options = GoogleGenAiChatOptions.builder()
            .model(model)
            .build();

        ChatResponse response = chatModel.call(new Prompt(aiMessages, options));

        log.info("Google AI Raw Response: {}", response);
        log.info("Google AI Result: {}", response.getResult());
        log.info("Google AI Response: {}", response.getResult().getOutput().getText());

            
        return response.getResult().getOutput().getText();
    }


    

}
