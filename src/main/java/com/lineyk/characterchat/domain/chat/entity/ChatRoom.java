package com.lineyk.characterchat.domain.chat.entity;

import com.lineyk.characterchat.domain.chatcharacter.entity.ChatCharacter;
import com.lineyk.characterchat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private ChatCharacter chatCharacter;

    @Column(columnDefinition = "TEXT")
    private String summaryMessage;

    @Column(nullable = false)
    private long affinityScore = 0;

    @Column(nullable = false)
    private int datingCount = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void increaseAffinity() {
        this.affinityScore++;
    }

    public void increaseDatingCount() {
        this.datingCount++;
    }

    public long getNextThreshold() {
        return (this.datingCount + 1) * 10L; 
    }

    public boolean isDatingAvailable() {
        return this.affinityScore >= getNextThreshold();
    }

    @Builder
    public ChatRoom(User user, ChatCharacter chatCharacter) {
        this.user = user;
        this.chatCharacter = chatCharacter;
    }
}
