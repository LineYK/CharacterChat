package com.lineyk.characterchat.global.ai.constant;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AiModelTest {

    @Test
    @DisplayName("AiModel의 GROQ 모델명과 제공자를 올바르게 반환하는지 테스트")
    void groqModelMapping() {
        // given
        AiModel model = AiModel.LLAMA_3_1_8B_INSTANT;

        // when & then
        assertThat(model.getModel()).isEqualTo("llama-3.1-8b-instant");
        assertThat(model.getProvider()).isEqualTo(AiProvider.GROQ);
    }

    @Test
    @DisplayName("AiModel의 Google 모델명과 제공자를 올바르게 반환하는지 테스트 ")
    void googleModelMapping() {
        // given
        AiModel model = AiModel.GEMINI_3_1_FLASH_PREVIEW;

        // when & then
        assertThat(model.getModel()).isEqualTo("gemini-3.1-flash-preview");
        assertThat(model.getProvider()).isEqualTo(AiProvider.GOOGLE);
    }


}
