package com.lineyk.characterchat.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChatRoom chatRoom;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sender senderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatProcessStatus processStatus; // AI 응답이 완료되어 클라이언트에 전송된 메시지인지 여부

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModified;

    @Builder
    public Chat(ChatRoom chatRoom, String message, Sender senderType) {
        this.chatRoom = chatRoom;
        this.message = message;
        this.senderType = senderType;
        this.processStatus = ChatProcessStatus.PENDING; // 기본값은 PENDING
    }

    public void updateProcessStatus(ChatProcessStatus status) {
        this.processStatus = status;
    }
}
