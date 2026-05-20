package com.lineyk.characterchat.domain.chat.service;

import java.time.LocalDateTime;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final AiModelFactory aiModelFactory;
    private final SimpMessagingTemplate messagingTemplate;

    private Chat requestAiResponse(UUID chatRoomId, AiModel aiModel) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        String systemPrompt = chatRoom.getChatCharacter().getPersona();

        // 최근 10개의 채팅 메시지만 가져와서 AI 모델에 전달
        List<Chat> chatHistory = chatRepository.findTop10ByChatRoomAndIsProcessedTrueOrderByCreatedAtDesc(chatRoom);
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
    public void processAiResponse(UUID userChatId, UUID chatRoomId, AiModel aiModel) {
        try {
            Chat aiChat = requestAiResponse(chatRoomId, aiModel);
            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, ChatMessage.from(aiChat));
        } catch (Exception e) {
            log.error("AI 응답 처리 중 해당 방({}) 오류 발생: {}", chatRoomId, e.getMessage(), e);

            chatRepository.findById(userChatId).ifPresent(chat -> {
                chat.updateProcessed(false);
            });

            // AI 응답 처리 중 예외가 발생한 경우, 클라이언트에게 에러 메시지 전송
            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, new ChatMessage(
                null,
                chatRoomId,
                "AI 응답 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
                Sender.SYSTEM,
                LocalDateTime.now()
            ));
        }
    }

    private AiMessage mapToAiMessage(Chat chat) {
        AiMessage.Role role =  (chat.getSenderType() == Sender.USER)
            ? AiMessage.Role.USER
            : AiMessage.Role.ASSISTANT;
        return new AiMessage(role, chat.getMessage());
    }

}
