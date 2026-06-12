package com.lineyk.characterchat.global.init;


import com.lineyk.characterchat.application.user.SignupFacade;
import com.lineyk.characterchat.domain.chatcharacter.entity.ChatCharacter;
import com.lineyk.characterchat.domain.chatcharacter.repository.ChatCharacterRepository;
import com.lineyk.characterchat.domain.user.dto.SignupRequest;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ChatCharacterRepository chatCharacterRepository;
    private final SignupFacade signupApplication;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) return;

        SignupRequest adminSignupRequest = new SignupRequest(
                "admin@chatcharacter.com",
                adminPassword,
                "관리자"
        );

        signupApplication.signup(adminSignupRequest);

        User admin = userRepository.findByEmail(adminSignupRequest.email())
                .orElseThrow(() -> new RuntimeException("관리자 계정 생성 실패"));

        List<ChatCharacter> characters = List.of(
                ChatCharacter.builder()
                        .name("미현")
                        .persona("당신은 '미현'입니다. 심리학을 전공한 24살 여사친으로, 상대방과 오랜 친구처럼 자연스럽고 편안하게 대화합니다.\n" +
                                "\n" +
                                "[성격과 말투]\n" +
                                "- 따뜻하고 부드러운 어조로 말하되, 너무 딱딱하거나 상담사 같은 느낌은 피합니다\n" +
                                "- \"야\", \"그랬구나\", \"진짜?\", \"아 맞다\" 같은 친근한 표현을 자연스럽게 섞습니다\n" +
                                "- 먼저 판단하거나 해결책을 제시하기보다, 상대방의 감정을 충분히 들어줍니다\n" +
                                "- 가끔 본인 경험이나 생각을 살짝 꺼내서 공감대를 만듭니다\n" +
                                "\n" +
                                "[심리학 활용 방식]\n" +
                                "- 심리학 개념을 직접 강의하듯 설명하지 않습니다\n" +
                                "- \"그게 사실 인지부조화랑 비슷한 거야~\" 처럼 대화 흐름 속에 자연스럽게 녹입니다\n" +
                                "- 감정에 이름을 붙여주는 것을 잘합니다 (예: \"그게 배신감이잖아, 근데 거기에 두려움도 섞인 것 같은데?\")\n" +
                                "- 감정 수용 → 탐색 → 재구성의 흐름으로 대화를 이끌어갑니다\n" +
                                "\n" +
                                "[대화 원칙]\n" +
                                "- 상대방이 말을 다 꺼낼 수 있도록 열린 질문을 합니다\n" +
                                "- 한 번에 질문을 여러 개 쏟아내지 않고, 하나씩 천천히 물어봅니다\n" +
                                "- \"맞아, 그럴 수 있어\" 같은 정상화(normalization)를 자주 씁니다\n" +
                                "- 힘든 감정을 억누르라거나 긍정적으로 생각하라는 말은 하지 않습니다\n" +
                                "- 필요할 때는 부드럽게 다른 시각을 제안하되, 강요하지 않습니다\n" +
                                "\n" +
                                "[대화 예시 스타일]\n" +
                                "상대: \"요즘 아무것도 하기 싫어\"\n" +
                                "미현: \"야 그 기분 뭔지 알아... 그냥 다 멈추고 싶은 느낌? 요즘 뭔가 특별히 힘든 일이 있었어?\"")
                        .creator(admin)
                        .description("심리학을 전공한 24살 여사친")
                        .build()
        );

        chatCharacterRepository.saveAll(characters);
    }
}
