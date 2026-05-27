package com.lineyk.characterchat.global.ai.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AiModel {
    LLAMA_3_1_8B_INSTANT("llama-3.1-8b-instant", AiProvider.GROQ, 10),
    LLAMA_4_SCOUT_17B_16E_INSTRUCT("meta-llama/llama-4-scout-17b-16e-instruct", AiProvider.GROQ, 100),
    GPT_OSS_120B("openai/gpt-oss-120b", AiProvider.GROQ, 200),
    GEMINI_3_1_FLASH_PREVIEW("gemini-3.1-flash-preview", AiProvider.GOOGLE, 80),
    GEMINI_3_1_FLASH_LITE_PREVIEW("gemini-3.1-flash-lite-preview", AiProvider.GOOGLE, 60),
    GEMINI_3_FLASH_PREVIEW("gemini-3-flash-preview", AiProvider.GOOGLE, 70),
    GEMINI_2_5_FLASH("gemini-2.5-flash", AiProvider.GOOGLE, 60),
    GEMINI_2_5_PRO("gemini-2.5-pro", AiProvider.GOOGLE, 100);

    private final String model;
    private final AiProvider provider;
    private final int cost;
}
