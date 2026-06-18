package com.lineyk.characterchat.domain.chat.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.chat.entity.Chat;
import com.lineyk.characterchat.domain.chat.entity.ChatProcessStatus;
import com.lineyk.characterchat.domain.chat.entity.ChatRoom;
import com.lineyk.characterchat.domain.chat.entity.Sender;
import com.lineyk.characterchat.domain.chat.repository.ChatRepository;
import com.lineyk.characterchat.domain.chat.repository.ChatRoomRepository;
import com.lineyk.characterchat.domain.chatcharacter.entity.CharacterImage;
import com.lineyk.characterchat.global.ai.constant.AiModel;
import com.lineyk.characterchat.global.ai.dto.AiMessage;
import com.lineyk.characterchat.global.ai.factory.AiModelFactory;
import com.lineyk.characterchat.global.ai.strategy.AiModelStrategy;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final AiModelFactory aiModelFactory;

    @Transactional
    public Chat requestAiResponse(UUID userChatId, UUID chatRoomId, AiModel aiModel, List<CharacterImage> images) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        String systemPrompt = chatRoom.getChatCharacter().getPersona();

        if (!images.isEmpty()) {
            systemPrompt += "\n\n" + buildImageInstructions(images);
        }

        // 최근 10개의 채팅 메시지만 가져와서 AI 모델에 전달
        List<Chat> chatHistory = chatRepository.findTop10ByChatRoomAndProcessStatusOrderByCreatedAtDesc(chatRoom, ChatProcessStatus.PROCESSED);
        Collections.reverse(chatHistory); // 시간 순서대로 정렬

        Chat userChat = chatRepository.findById(userChatId)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND));

        chatHistory.add(userChat); // 최신 사용자 메시지도 포함
        
        List<AiMessage> aiHistory = chatHistory.stream()
            .map(this::mapToAiMessage)
            .toList();

        AiModelStrategy strategy = aiModelFactory.getStrategy(aiModel.getProvider());

        log.info("Requesting AI response with model: {}, systemPrompt: {}, chatHistory: {}", aiModel.getModel(), systemPrompt, aiHistory);

        String aiResponse = strategy.chat(
            aiModel.getModel(),
            systemPrompt,
            aiHistory
        );

        Chat aiChat = Chat.builder()
            .chatRoom(chatRoom)
            .senderType(Sender.CHARACTER)
            .message(aiResponse)
            .build();
            
        aiChat.process();
        chatRepository.save(aiChat);
        return aiChat;
    }

    private AiMessage mapToAiMessage(Chat chat) {
        AiMessage.Role role =  (chat.getSenderType() == Sender.USER)
            ? AiMessage.Role.USER
            : AiMessage.Role.ASSISTANT;
        return new AiMessage(role, chat.getMessage());
    }

    private String buildImageInstructions(List<CharacterImage> images) {
        StringBuilder sb = new StringBuilder("감정이나 상황에 맞게 [img:태그] 형식을 대화 중간에 삽입해:\n");
        sb.append("사용 가능한 태크: \n");
        images.forEach(image -> {
            sb.append("- [img:").append(image.getEmotionTag()).append("]");
            if(image.getDescription() != null && !image.getDescription().isEmpty()) {
                sb.append(" : ").append(image.getDescription());
            }
            sb.append("\n");
        });

        return sb.toString();
    }

}
