package com.lineyk.characterchat.domain.chatcharacter.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.chatcharacter.dto.ChatCharacterResponse;
import com.lineyk.characterchat.domain.chatcharacter.repository.ChatCharacterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatCharacterService {

    private final ChatCharacterRepository chatCharacterRepository;

    public List<ChatCharacterResponse> getAllChatCharacters() {
        return chatCharacterRepository.findAll()
            .stream()
            .map(ChatCharacterResponse::from)
            .toList();
    }
    
}