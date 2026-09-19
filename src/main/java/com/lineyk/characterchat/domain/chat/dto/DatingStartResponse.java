package com.lineyk.characterchat.domain.chat.dto;

import java.util.UUID;

import com.lineyk.characterchat.domain.chat.entity.ChatRoom;

public record DatingStartResponse(
    UUID chatRoomId,
    String characterName,
    String vrmModelUrl
) {
    public static DatingStartResponse of(ChatRoom chatRoom) {
        return new DatingStartResponse(
            chatRoom.getId(),
            chatRoom.getChatCharacter().getName(),
            chatRoom.getChatCharacter().getVrmModelUrl()
        );
    }
}
