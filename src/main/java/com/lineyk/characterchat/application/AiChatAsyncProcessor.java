package com.lineyk.characterchat.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.Sender;
import com.lineyk.characterchat.domain.chat.service.AiChatService;
import com.lineyk.characterchat.domain.chat.service.ChatService;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import com.lineyk.characterchat.global.ai.constant.AiModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatAsyncProcessor {
    
    private final AiChatService aiService;
    private final ChatService chatService;
    private final WalletService walletService;
    private final SimpMessagingTemplate messagingTemplate;

    
    public void processAiResponse(UUID userChatId, UUID chatRoomId, UUID userId, AiModel aiModel) {
        try {
            log.info("processAiResponse thread={}", Thread.currentThread().getName());
            Chat aiChat = aiService.requestAiResponse(userChatId, chatRoomId, aiModel);

            // 사용자 메시지와 크레딧 사용 확정 처리
            chatService.markAsProcessed(userChatId);
            walletService.confirmCredits(userChatId);

            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, ChatMessage.from(aiChat));
        } catch (Exception e) {
            log.error("AI 응답 처리 중 해당 방({}) 오류 발생: {}", chatRoomId, e.getMessage(), e);
            
            // 사용자 메시지와 크레딧 사용 실패 처리
            chatService.markAsUnprocessed(userChatId);
            walletService.failCredits(userChatId);

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
