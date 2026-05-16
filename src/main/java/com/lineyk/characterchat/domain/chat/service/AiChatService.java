package com.lineyk.characterchat.domain.chat.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.ChatRoom;
import com.lineyk.characterchat.domain.chat.entity.Sender;
import com.lineyk.characterchat.domain.chat.repository.ChatRepository;
import com.lineyk.characterchat.domain.chat.repository.ChatRoomRepository;
import com.lineyk.characterchat.global.ai.constant.AiModel;
import com.lineyk.characterchat.global.ai.dto.AiMessage;
import com.lineyk.characterchat.global.ai.factory.AiModelFactory;
import com.lineyk.characterchat.global.ai.strategy.AiModelStrategy;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final AiModelFactory aiModelFactory;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    private Chat requestAiResponse(UUID chatRoomId, AiModel aiModel) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        String systemPrompt = chatRoom.getChatCharacter().getPersona();

        // 최근 10개의 채팅 메시지만 가져와서 AI 모델에 전달
        List<Chat> chatHistory = chatRepository.findTop10ByChatRoomOrderByCreatedAtDesc(chatRoom);
        Collections.reverse(chatHistory); // 시간 순서대로 정렬

        List<AiMessage> aiHistory = chatHistory.stream()
            .map(this::mapToAiMessage)
            .toList();

        AiModelStrategy strategy = aiModelFactory.getStrategy(aiModel.getProvider());
        String aiResponse = strategy.chat(
            aiModel.getModel(),
            systemPrompt,
            aiHistory
        );

        Chat aiChat = Chat.builder()
            .chatRoom(chatRoom)
            .senderType(Sender.CHARACTER)
            .message(aiResponse)
            .build();
        chatRepository.save(aiChat);
        return aiChat;
    }

    @Async
    @Transactional    
    public void processAiResponse(UUID chatRoomId, AiModel aiModel) {
        Chat aiChat = requestAiResponse(chatRoomId, aiModel);
        String aiResponse = aiChat.getMessage();

        ChatMessage aiChatMessage = new ChatMessage(
            chatRoomId,
            aiResponse,
            Sender.CHARACTER,
            aiChat.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, aiChatMessage);
    }

    private AiMessage mapToAiMessage(Chat chat) {
        AiMessage.Role role =  (chat.getSenderType() == Sender.USER)
            ? AiMessage.Role.USER
            : AiMessage.Role.ASSISTANT;
        return new AiMessage(role, chat.getMessage());
    }

}
