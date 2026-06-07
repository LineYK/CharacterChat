package com.lineyk.characterchat.domain.chat.controller;

import com.lineyk.characterchat.application.ChatFacade;
import com.lineyk.characterchat.domain.chat.dto.ChatRequest;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.global.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatFacade chatFacade;

    @MessageMapping("/chat/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable("chatRoomId") UUID chatRoomId,
            ChatRequest request,
            Principal principal
    ) {

        UsernamePasswordAuthenticationToken auth =  (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.user();

        chatFacade.sendUserMessage(request, chatRoomId, user);
    }

}
