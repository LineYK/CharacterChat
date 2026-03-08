package com.lineyk.characterchat.domain.chat.repository;

import com.lineyk.characterchat.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

}
