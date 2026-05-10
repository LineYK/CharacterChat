package com.lineyk.characterchat.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),

    // JWT 토큰
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),

    // 캐릭터
    CHARACTER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 캐릭터입니다."),

    // 채팅
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),
    CHATROOM_ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없는 채팅방입니다."),
    
    // AI
    AI_PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 AI 공급자입니다."),
    AI_MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 AI 모델입니다.");

    private final HttpStatus status;
    private final String message;

}
