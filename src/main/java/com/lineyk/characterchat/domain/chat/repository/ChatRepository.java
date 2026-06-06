package com.lineyk.characterchat.domain.chat.repository;

import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.ChatProcessStatus;
import com.lineyk.characterchat.domain.chat.entity.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    List<Chat> findByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom);

    List<Chat> findTop10ByChatRoomAndProcessStatusOrderByCreatedAtDesc(ChatRoom chatRoom, ChatProcessStatus processStatus);
    
}
