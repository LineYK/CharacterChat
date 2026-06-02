package com.lineyk.characterchat.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.dto.ChatRequest;
import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.Sender;
import com.lineyk.characterchat.domain.chat.service.AiChatService;
import com.lineyk.characterchat.domain.chat.service.ChatService;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import com.lineyk.characterchat.global.ai.constant.AiModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatApplication {
    
    private final ChatService chatService;
    private final AiChatService aiService;
    private final WalletService walletService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ChatMessage sendUserMessage(ChatRequest request, UUID chatRoomId, User user) {
        ChatMessage saved = chatService.sendUserMessage(chatRoomId, user, request.message(), request.model());
        walletService.spendCredits(user.getId(), request.model().getCost(), chatRoomId);
        messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, saved);

        processAiResponse(saved.chatId(), chatRoomId, request.model());
        return saved;
    }

    @Async
    @Transactional    
    public void processAiResponse(UUID userChatId, UUID chatRoomId, AiModel aiModel) {
        try {
            Chat aiChat = aiService.requestAiResponse(chatRoomId, aiModel);
            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, ChatMessage.from(aiChat));
        } catch (Exception e) {
            log.error("AI 응답 처리 중 해당 방({}) 오류 발생: {}", chatRoomId, e.getMessage(), e);
            // AI 응답 처리 실패 시, 해당 메시지를 미처리 상태
            aiService.markAsUnprocessed(userChatId);
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
    

}
