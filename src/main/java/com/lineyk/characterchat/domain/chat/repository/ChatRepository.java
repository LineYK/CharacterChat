package com.lineyk.characterchat.domain.chat.repository;

import com.lineyk.characterchat.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

}
