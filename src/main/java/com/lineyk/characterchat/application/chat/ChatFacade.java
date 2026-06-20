package com.lineyk.characterchat.application.chat;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.Sender;
import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.dto.ChatRequest;
import com.lineyk.characterchat.domain.chat.dto.MessageSegment;
import com.lineyk.characterchat.domain.chat.event.ChatSavedEvent;
import com.lineyk.characterchat.domain.chat.service.ChatService;
import com.lineyk.characterchat.domain.chatcharacter.entity.CharacterImage;
import com.lineyk.characterchat.domain.chatcharacter.repository.CharacterImageRepository;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import com.lineyk.characterchat.global.util.ImageTagParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatFacade {

    private final ChatService chatService;
    private final WalletService walletService;
    private final CharacterImageRepository characterImageRepository;

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
                request.model()));
        return saved;
    }

    public List<ChatMessage> getChatMessages(UUID chatRoomId, User user) {
        List<Chat> chats = chatService.getChatMessages(chatRoomId, user);

        List<CharacterImage> images = characterImageRepository.findByChatRoomId(chatRoomId);
        Map<String, String> tagToUrlMap = images.stream()
                .collect(Collectors.toMap(CharacterImage::getEmotionTag, CharacterImage::getImageUrl,
                        (existing, replacement) -> existing)); // 중복 태그 처리

        return chats.stream()
                .map(chat -> {
                    if (chat.getSenderType() == Sender.CHARACTER) {
                        List<MessageSegment> segments = ImageTagParser.parse(chat.getMessage(), tagToUrlMap);
                        return ChatMessage.fromAi(chat, segments);
                    }
                    return ChatMessage.from(chat);
                }).toList();
    }

}
