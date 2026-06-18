package com.lineyk.characterchat.domain.chatcharacter.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lineyk.characterchat.domain.chatcharacter.entity.CharacterImage;

public interface CharacterImageRepository extends JpaRepository<CharacterImage, UUID> {

    List<CharacterImage> findByChatCharacterId(UUID chatCharacterId);

    @Query("SELECT ci FROM CharacterImage ci WHERE ci.chatRoomId = " +
              "(SELECT cr.chatCharacter.id FROM ChatRoom cr WHERE cr.id = :chatRoomId)")
    List<CharacterImage> findByChatRoomId(@Param("chatRoomId") UUID chatRoomId);
}