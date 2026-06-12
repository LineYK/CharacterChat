package com.lineyk.characterchat.application.chat;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.dto.ChatRequest;
import com.lineyk.characterchat.domain.chat.event.ChatSavedEvent;
import com.lineyk.characterchat.domain.chat.service.ChatService;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatFacade {
    
    private final ChatService chatService;
    private final WalletService walletService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ChatMessage sendUserMessage(ChatRequest request, UUID chatRoomId, User user) {
        log.info("sendUserMessage thread={}", Thread.currentThread().getName());
        ChatMessage saved = chatService.sendUserMessage(chatRoomId, user, request.message(), request.model());
        walletService.reserveCredits(user.getId(), request.model().getCost(), saved.chatId());
        messagingTemplate.convertAndSend("/sub/chat/" + chatRoomId, saved);

        // 채팅 저장 후 이벤트 발행
        eventPublisher.publishEvent(new ChatSavedEvent(
            saved.chatId(), 
            chatRoomId, 
            user.getId(), 
            request.model()
        ));
        return saved;
    }

}
