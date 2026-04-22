package com.lineyk.characterchat.domain.chat.controller;

import com.lineyk.characterchat.domain.chat.dto.ChatMessage;
import com.lineyk.characterchat.domain.chat.service.ChatService;
import com.lineyk.characterchat.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable UUID chatRoomId,
            ChatMessage request,
            Principal principal
    ) {
        User dummy = User.builder()
                .build();
        ChatMessage savedMessage = chatService.sendUserMessage(chatRoomId, dummy, request.message());
        messagingTemplate.convertAndSend("/sub/chat" + chatRoomId, savedMessage);
    }


}
