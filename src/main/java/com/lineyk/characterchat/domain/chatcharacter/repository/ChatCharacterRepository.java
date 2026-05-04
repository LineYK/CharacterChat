package com.lineyk.characterchat.domain.chatcharacter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lineyk.characterchat.domain.chatcharacter.entity.ChatCharacter;

import java.util.UUID;

public interface ChatCharacterRepository extends JpaRepository<ChatCharacter, UUID> {

}
