package com.lineyk.characterchat.application.chat;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.lineyk.characterchat.domain.chat.event.ChatSavedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSavedEventListeners {
    
    private final AiChatAsyncProcessor aiChatAsyncProcessor;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatSavedEvent(ChatSavedEvent event) {
        log.info("ChatSavedEvent: {}", event);
        aiChatAsyncProcessor.processAiResponse(
            event.chatId(), 
            event.chatRoomId(), 
            event.userId(), 
            event.aiModel()
        );
    }
}
