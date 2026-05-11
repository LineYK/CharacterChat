package com.lineyk.characterchat.global.ai.dto;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AiMessageTest {
    
    @Test
    @DisplayName("User 역할의 AiMessage를 Spring AI의 UserMessage로 변환하는 테스트")
    void toSpringAiMessageUser() {
        AiMessage aiMessage = new AiMessage(AiMessage.Role.USER, "유저 메시지입니다.");
        Message result = aiMessage.toSpringAiMessage();
        
        assertThat(result).isInstanceOf(UserMessage.class);
        assertThat(result.getText()).isEqualTo("유저 메시지입니다.");
    }

    @Test
    @DisplayName("Assistant 역할의 AiMessage를 Spring AI의 Message로 변환하는 테스트")
    void toSpringAiMessageAssistant() {
        AiMessage aiMessage = new AiMessage(AiMessage.Role.ASSISTANT, "어시스턴트 메시지입니다.");
        Message result = aiMessage.toSpringAiMessage();

        assertThat(result).isInstanceOf(Message.class);
        assertThat(result.getText()).isEqualTo("어시스턴트 메시지입니다.");
    }

    @Test
    @DisplayName("System 역할의 AiMessage를 Spring AI의 Message로 변환하는 테스트")
    void toSpringAiMessageSystem() {
        AiMessage aiMessage = new AiMessage(AiMessage.Role.SYSTEM, "시스템 메시지입니다.");
        Message result = aiMessage.toSpringAiMessage();

        assertThat(result).isInstanceOf(Message.class);
        assertThat(result.getText()).isEqualTo("시스템 메시지입니다.");
    }

}
