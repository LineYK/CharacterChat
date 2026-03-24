package com.lineyk.characterchat.global.error;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String message,
        String code
) {
    public static  ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getMessage(), errorCode.name());
    }
}
