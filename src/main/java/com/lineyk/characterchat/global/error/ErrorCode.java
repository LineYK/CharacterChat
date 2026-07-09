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
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채팅입니다."),
    
    // AI
    AI_PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 AI 공급자입니다."),
    AI_MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 AI 모델입니다."),
    
    // 지갑
    WALLET_NOT_FOUND(HttpStatus.NOT_FOUND, "지갑이 존재하지 않습니다."),
    INSUFFICIENT_CREDITS(HttpStatus.BAD_REQUEST, "크레딧이 부족합니다."),

    // 거래
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 거래입니다."),
    INVALID_TRANSACTION_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 거래 상태입니다."),

    // 결제
    INVALID_PAYMENT_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 결제 상태입니다."),
    INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "유효하지 않은 결제 수단입니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 결제입니다."),
    PAYMENT_CONFIRM_FAILED(HttpStatus.BAD_REQUEST, "결제 확인에 실패했습니다."),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "결제 금액이 일치하지 않습니다."),
    PAYMENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "결제에 대한 접근 권한이 없습니다."),
    PAYMENT_CANCEL_FAILED(HttpStatus.BAD_REQUEST, "결제 취소에 실패했습니다."),
    REFUND_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST, "환불 가능 기간이 만료되었습니다."),

    // 구독
    SUBSCRIPTION_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 구독 플랜입니다."),
    SUBSCRIPTION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 구독입니다."),
    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 구독입니다.");


    private final HttpStatus status;
    private final String message;

}
