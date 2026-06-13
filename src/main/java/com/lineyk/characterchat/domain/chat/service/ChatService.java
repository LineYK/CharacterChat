package com.lineyk.characterchat.domain.chat.service;

import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.ChatProcessStatus;
import com.lineyk.characterchat.domain.chat.entity.ChatRoom;
import com.lineyk.characterchat.domain.chat.entity.Sender;
import com.lineyk.characterchat.domain.chat.repository.ChatRepository;
import com.lineyk.characterchat.domain.chat.repository.ChatRoomRepository;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.global.ai.constant.AiModel;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatMessage sendUserMessage(UUID chatRoomId, User user, String message, AiModel aiModel) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        if (!chatRoom.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.CHATROOM_ACCESS_DENIED);
        }

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .message(message)
                .senderType(Sender.USER)
                .build();

        chatRepository.save(chat);

        return new ChatMessage(chat.getId(), chatRoomId, message, Sender.USER, chat.getCreatedAt());
    }

    public List<ChatMessage> getChatMessages(UUID chatRoomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        if (!chatRoom.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.CHATROOM_ACCESS_DENIED);
        }

        List<Chat> chats = chatRepository.findByChatRoomOrderByCreatedAtAsc(chatRoom);
        return chats.stream()
                .map(ChatMessage::from)
                .toList();
    }

    @Transactional
    public void markAsProcessed(UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND));
        chat.process();
    }

    @Transactional
    public void markAsUnprocessed(UUID chatId) {
        Chat chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND));
        chat.unprocess();
    }

    @Transactional
    public void recoverPendingChats() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(10);
        chatRepository.findByProcessStatusAndCreatedAtBefore(ChatProcessStatus.PENDING, cutoff)
            .forEach(chat -> chat.unprocess());
    }
}
