package com.lineyk.characterchat.domain.user.dto;

public record LoginResponse(
        String accessToken
) {
    public static LoginResponse of(String accessToken) {
        return new LoginResponse(accessToken);
    }
}
