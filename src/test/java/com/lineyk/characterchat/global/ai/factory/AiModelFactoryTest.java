package com.lineyk.characterchat.global.ai.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lineyk.characterchat.global.ai.constant.AiProvider;
import com.lineyk.characterchat.global.ai.strategy.AiModelStrategy;
import com.lineyk.characterchat.global.error.CustomException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AiModelFactoryTest {
    
    private AiModelFactory aiModelFactory;

    @Mock
    private AiModelStrategy groqStrategy;

    @Mock
    private AiModelStrategy googleStrategy;

    @BeforeEach
    void setUp() {
        given(groqStrategy.provider()).willReturn(AiProvider.GROQ);
        given(googleStrategy.provider()).willReturn(AiProvider.GOOGLE);

        aiModelFactory = new AiModelFactory(
            List.of(groqStrategy, googleStrategy)
        );
    }

    @Test
    @DisplayName("Groq Provider를 요청하면 Groq Strategy를 반환한다.")
    void getStrategyGroq() {
        AiModelStrategy result = aiModelFactory.getStrategy(AiProvider.GROQ);

        assertThat(result).isEqualTo(groqStrategy);
    }

    @Test
    @DisplayName("Google Provider를 요청하면 Google Strategy를 반환한다.")
    void getStrategyGoogle() {
        AiModelStrategy result = aiModelFactory.getStrategy(AiProvider.GOOGLE);

        assertThat(result).isEqualTo(googleStrategy);
    }

    @Test
    @DisplayName("존재하지 않는 Provider를 요청하면 예외가 발생한다.")
    void getStrategyNotFound() {

        AiModelFactory emptyFactory = new AiModelFactory(List.of());

        assertThatThrownBy(() -> emptyFactory.getStrategy(AiProvider.GROQ))
            .isInstanceOf(CustomException.class);
    }
        
}
