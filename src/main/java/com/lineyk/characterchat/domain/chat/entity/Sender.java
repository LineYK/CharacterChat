package com.lineyk.characterchat.domain.chat.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Sender {
    USER("SENDER_USER", "사용자"),
    CHARACTER("SENDER_CHARACTER", "캐릭터"),
    SYSTEM("SENDER_SYSTEM", "시스템");

    private final String key;
    private final String title;

}
