package com.lineyk.characterchat.global.ai.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AiModel {
    LLAMA_3_1_8B_INSTANT("llama-3.1-8b-instant", AiProvider.GROQ),
    LLAMA_4_SCOUT_17B_16E_INSTRUCT("meta-llama/llama-4-scout-17b-16e-instruct", AiProvider.GROQ),
    GPT_OSS_120B("openai/gpt-oss-120b", AiProvider.GROQ),
    GEMINI_3_1_FLASH_PREVIEW("gemini-3.1-flash-preview", AiProvider.GOOGLE),
    GEMINI_3_1_FLASH_LITE_PREVIEW("gemini-3.1-flash-lite-preview", AiProvider.GOOGLE),
    GEMINI_3_FLASH_PREVIEW("gemini-3-flash-preview", AiProvider.GOOGLE),
    GEMINI_2_5_FLASH("gemini-2.5-flash", AiProvider.GOOGLE),
    GEMINI_2_5_PRO("gemini-2.5-pro", AiProvider.GOOGLE);

    private final String model;
    private final AiProvider provider;
}
