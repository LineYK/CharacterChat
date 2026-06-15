package com.lineyk.characterchat.domain.chatcharacter.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lineyk.characterchat.domain.chatcharacter.entity.CharacterImage;

public interface CharacterImageRepository extends JpaRepository<CharacterImage, UUID> {

    List<CharacterImage> findByChatCharacterId(UUID chatCharacterId);
}