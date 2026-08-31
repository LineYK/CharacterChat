package com.lineyk.characterchat.domain.chat.repository;

import com.lineyk.characterchat.domain.chat.entity.ChatRoom;
import com.lineyk.characterchat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    List<ChatRoom> findByUserOrderByUpdatedAtDesc(User user);

    Optional<ChatRoom> findFirstByUserAndChatCharacterIdOrderByUpdatedAtDesc(User user, UUID chatCharacterId);

}
