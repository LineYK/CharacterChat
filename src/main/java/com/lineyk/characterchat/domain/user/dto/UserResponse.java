package com.lineyk.characterchat.domain.user.dto;

import com.lineyk.characterchat.domain.user.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String email,
        String nickname,
        long credits
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname(), 0L);
    }

    public static UserResponse from(User user, long credits) {
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname(), credits);
    }
}
