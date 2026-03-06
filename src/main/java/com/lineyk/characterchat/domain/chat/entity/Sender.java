package com.lineyk.characterchat.domain.chat.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Sender {
    USER("SENDER_USER", "사용자"),
    CHARACTER("SENDER_CHARACTER", "캐릭터");

    private final String key;
    private final String title;

}
