package com.lineyk.characterchat.domain.chat.service;

import com.lineyk.characterchat.domain.chat.dto.ChatRoomCreateRequest;
import com.lineyk.characterchat.domain.chat.dto.ChatRoomResponse;
import com.lineyk.characterchat.domain.chat.entity.ChatRoom;
import com.lineyk.characterchat.domain.chat.repository.ChatRoomRepository;
import com.lineyk.characterchat.domain.chatcharactor.entity.ChatCharacter;
import com.lineyk.characterchat.domain.chatcharactor.repository.ChatCharacterRepository;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatCharacterRepository chatCharacterRepository;

    @Transactional
    public ChatRoomResponse createChatRoom(User user, ChatRoomCreateRequest request) {
        ChatCharacter chatCharacter = chatCharacterRepository.findById(request.characterId()).orElseThrow(() -> new CustomException(ErrorCode.CHARACTER_NOT_FONUD));

        ChatRoom chatRoom = ChatRoom.builder()
                .user(user)
                .chatCharacter(chatCharacter)
                .build();

        chatRoomRepository.save(chatRoom);

        return ChatRoomResponse.from(chatRoom);
    }
}
