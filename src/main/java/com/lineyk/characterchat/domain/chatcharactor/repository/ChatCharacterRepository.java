package com.lineyk.characterchat.domain.chatcharactor.repository;

import com.lineyk.characterchat.domain.chatcharactor.entity.ChatCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatCharacterRepository extends JpaRepository<ChatCharacter, UUID> {

}
