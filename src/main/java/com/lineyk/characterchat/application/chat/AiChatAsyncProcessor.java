package com.lineyk.characterchat.application.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.dto.MessageSegment;
import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.Sender;
import com.lineyk.characterchat.domain.chat.service.AiChatService;
import com.lineyk.characterchat.domain.chat.service.ChatService;
import com.lineyk.characterchat.domain.chatcharacter.entity.CharacterImage;
import com.lineyk.characterchat.domain.chatcharacter.repository.CharacterImageRepository;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import com.lineyk.characterchat.global.ai.constant.AiModel;
import com.lineyk.characterchat.global.util.ImageTagParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatAsyncProcessor {
    
    private final AiChatService aiService;
    private final ChatService chatService;
    private final WalletService walletService;
    private final CharacterImageRepository characterImageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    
    public void processAiResponse(UUID userChatId, UUID chatRoomId, UUID userId, AiModel aiModel) {
        try {
            log.info("processAiResponse thread={}", Thread.currentThread().getName());

            List<CharacterImage> images = characterImageRepository.findByChatRoomId(chatRoomId);

            Chat aiChat = aiService.requestAiResponse(userChatId, chatRoomId, aiModel, images);

            // 사용자 메시지와 크레딧 사용 확정 처리
            chatService.markAsProcessed(userChatId);
            walletService.confirmCredits(userChatId);

            Map<String, String> tagToUrlMap = images.stream()
                .collect(Collectors.toMap(CharacterImage::getEmotionTag, CharacterImage::getImageUrl, 
                          (existing, replacement) -> existing)); // 중복 태그 처리

            List<MessageSegment> segments = ImageTagParser.parse(aiChat.getMessage(), tagToUrlMap);

            messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, ChatMessage.fromAi(aiChat, segments));
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
                LocalDateTime.now(),
                null
            ));
        }
    }
}
