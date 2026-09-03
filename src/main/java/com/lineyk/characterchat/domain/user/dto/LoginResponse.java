package com.lineyk.characterchat.domain.user.dto;

import java.util.Date;

import com.lineyk.characterchat.domain.user.entity.User;

public record LoginResponse(
        String accessToken,
        String tokenType,
        String email,
        String nickname,
        Date expirationDate
) {
    public static LoginResponse of(String accessToken, User user, Date expirationDate) {
        return new LoginResponse(
                accessToken,
                "Bearer",
                user.getEmail(),
                user.getNickname(),
                expirationDate
        );
    }
}
