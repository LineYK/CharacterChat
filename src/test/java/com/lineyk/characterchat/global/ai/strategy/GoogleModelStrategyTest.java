package com.lineyk.characterchat.global.ai.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;

import com.lineyk.characterchat.global.ai.constant.AiProvider;
import com.lineyk.characterchat.global.ai.dto.AiMessage;

@ExtendWith(MockitoExtension.class)
public class GoogleModelStrategyTest {

    @Mock
    private GoogleGenAiChatModel chatModel;

    @InjectMocks
    private GoogleModelStrategy googleModelStrategy;
    
    @Test
    @DisplayName("Google 제공자가 옮바르게 매핑되는지 확인")
    void testProvider() {
        assertThat(googleModelStrategy.provider())
            .isEqualTo(AiProvider.GOOGLE);
    }

    @Test
    @DisplayName("채팅 요청 시 페르소나, 대화 이력, 현재 메시지가 포함된 Prompt를 생성하여 AI 호출")
    void chatSuccess() {
        String modelName = "gemini-1.5-pro";
        String systemPrompt = "넌 친절한 조수야.";
        List<AiMessage> messages = List.of(
            new AiMessage(AiMessage.Role.USER, "이전 질문"),
            new AiMessage(AiMessage.Role.ASSISTANT, "이전 답변"),
            new AiMessage(AiMessage.Role.USER, "현재 질문")
        );

        AssistantMessage assistantMessage = new AssistantMessage("가짜 AI 응답");
        Generation generation = new Generation(assistantMessage);
        ChatResponse mockResponse = new ChatResponse(List.of(generation));

        given(chatModel.call(any(Prompt.class))).willReturn(mockResponse);

        ArgumentCaptor<Prompt> promptCaptor = ArgumentCaptor.forClass(Prompt.class);
        String response = googleModelStrategy.chat(modelName, systemPrompt, messages);

        assertThat(response).isEqualTo("가짜 AI 응답");

        // Prompt 객체가 올바르게 생성되었는지 검증
        verify(chatModel).call(promptCaptor.capture());
        Prompt capturedPrompt = promptCaptor.getValue();

        // 시스템 메시지 검증
        assertThat(capturedPrompt.getInstructions()).hasSize(4);
        assertThat(capturedPrompt.getInstructions().get(0)).isInstanceOf(SystemMessage.class);
        assertThat(capturedPrompt.getInstructions().get(1)).isInstanceOf(UserMessage.class);
        assertThat(capturedPrompt.getInstructions().get(2)).isInstanceOf(AssistantMessage.class);
        assertThat(capturedPrompt.getInstructions().get(3)).isInstanceOf(UserMessage.class);

        GoogleGenAiChatOptions options = (GoogleGenAiChatOptions) capturedPrompt.getOptions();
        assertThat(options.getModel()).isEqualTo(modelName); 
    }

}
