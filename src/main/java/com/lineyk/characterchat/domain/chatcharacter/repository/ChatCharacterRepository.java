package com.lineyk.characterchat.domain.chatcharacter.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lineyk.characterchat.domain.chatcharacter.entity.ChatCharacter;

import java.util.List;
import java.util.UUID;

public interface ChatCharacterRepository extends JpaRepository<ChatCharacter, UUID> {

    @EntityGraph(attributePaths = { "creator" })
    @Override
    List<ChatCharacter> findAll();

    @Query("""
            select c, ci
            from ChatCharacter c
            join fetch c.creator creator
            left join CharacterImage ci
                on ci.chatCharacter = c
                and ci.emotionTag = :emotionTag
            """)
    List<Object[]> findAllWithProfileImage(@Param("emotionTag") String emotionTag);
}
