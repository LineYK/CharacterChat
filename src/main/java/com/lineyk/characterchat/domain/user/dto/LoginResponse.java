package com.lineyk.characterchat.domain.user.dto;

import com.lineyk.characterchat.domain.user.entity.User;

public record LoginResponse(
        String accessToken,
        String tokenType,
        String email,
        String nickname
) {
    public static LoginResponse of(String accessToken, User user) {
        return new LoginResponse(
                accessToken,
                "Bearer",
                user.getEmail(),
                user.getNickname()
        );
    }
}
