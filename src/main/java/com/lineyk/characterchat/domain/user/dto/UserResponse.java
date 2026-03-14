package com.lineyk.characterchat.domain.user.dto;

import com.lineyk.characterchat.domain.user.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String email,
        String nickname
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getNickname());
    }
}
