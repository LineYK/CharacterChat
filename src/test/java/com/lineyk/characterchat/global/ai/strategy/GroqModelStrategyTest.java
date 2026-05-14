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
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;

import com.lineyk.characterchat.global.ai.dto.AiMessage;

@ExtendWith(MockitoExtension.class)
public class GroqModelStrategyTest {
    
    @Mock
    private OpenAiChatModel chatModel;

    @InjectMocks
    private GroqModelStrategy groqModelStrategy;

    @Test
    @DisplayName("채팅 요청 시 페르소나, 대화 이력, 현재 메시지가 포함된 Prompt를 생성하여 AI 호출")
    void chatSuccess() {
        // given
        String modelName = "llama-3.1-8b-instant";
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

        // Prompt를 낚아채기 위한 ArgumentCaptor
        ArgumentCaptor<Prompt> promptCaptor = ArgumentCaptor.forClass(Prompt.class);

        // when
        String response = groqModelStrategy.chat(modelName, systemPrompt, messages);

        // then
        assertThat(response).isEqualTo("가짜 AI 응답");

        verify(chatModel).call(promptCaptor.capture());
        Prompt capturedPrompt = promptCaptor.getValue();

        assertThat(capturedPrompt.getInstructions()).hasSize(4);
        assertThat(capturedPrompt.getInstructions().get(0)).isInstanceOf(SystemMessage.class);
        assertThat(capturedPrompt.getInstructions().get(1)).isInstanceOf(UserMessage.class);
        assertThat(capturedPrompt.getInstructions().get(2)).isInstanceOf(AssistantMessage.class);
        assertThat(capturedPrompt.getInstructions().get(3)).isInstanceOf(UserMessage.class);

        OpenAiChatOptions options = (OpenAiChatOptions) capturedPrompt.getOptions();
        assertThat(options.getModel()).isEqualTo(modelName);
    }


}
