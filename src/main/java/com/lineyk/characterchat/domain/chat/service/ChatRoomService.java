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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatCharacterRepository chatCharacterRepository;

    @Transactional
    public ChatRoomResponse createChatRoom(User user, ChatRoomCreateRequest request) {
        ChatCharacter chatCharacter = chatCharacterRepository.findById(request.characterId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHARACTER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.builder()
                .user(user)
                .chatCharacter(chatCharacter)
                .build();

        chatRoomRepository.save(chatRoom);

        return ChatRoomResponse.from(chatRoom);
    }

    public List<ChatRoomResponse> getChatRooms(User user) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserOrderByCreatedAtDesc(user);
        return chatRooms.stream()
                .map(ChatRoomResponse::from)
                .toList();
    }

    public ChatRoomResponse getChatRoom(UUID chatRoomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        if (!chatRoom.getUser().getId().equals(user.getId()))
            throw new CustomException(ErrorCode.CHATROOM_ACCESS_DENIED);

        return ChatRoomResponse.from(chatRoom);
    }


}
