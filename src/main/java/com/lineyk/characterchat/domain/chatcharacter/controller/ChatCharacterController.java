package com.lineyk.characterchat.domain.chatcharacter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lineyk.characterchat.domain.chatcharacter.dto.ChatCharacterResponse;
import com.lineyk.characterchat.domain.chatcharacter.service.ChatCharacterService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/characters")
public class ChatCharacterController {

    private final ChatCharacterService chatCharacterService;

    @Operation(
        summary = "채팅 캐릭터 목록 조회",
        description = "모든 채팅 캐릭터의 목록을 조회합니다.",
        security = {}
    )
    @GetMapping
    public List<ChatCharacterResponse> getAllChatCharacters() {
        return chatCharacterService.getAllChatCharacters();
    }
}
