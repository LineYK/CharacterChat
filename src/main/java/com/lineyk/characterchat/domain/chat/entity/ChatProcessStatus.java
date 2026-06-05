package com.lineyk.characterchat.domain.chat.entity;

public enum ChatProcessStatus {
    PROCESSED, // AI 응답이 완료되어 클라이언트에 전송된 메시지
    PENDING,   // AI 응답이 처리 중인 메시지
    UNPROCESSED // AI 응답이 아직 처리되지 않은 메시지
}
