package com.lineyk.characterchat.domain.chatcharacter.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_character_id", nullable = false)
    private ChatCharacter chatCharacter;

    @Column(nullable = false)
    private String emotionTag;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private String description;  // 시스템 프롬프트를 위한 이미지에 대한 설명 

    @Builder
    public CharacterImage(ChatCharacter chatCharacter, String emotionTag, String imageUrl, String description) {
        this.chatCharacter = chatCharacter;
        this.emotionTag = emotionTag;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
