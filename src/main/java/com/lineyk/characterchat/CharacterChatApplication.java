package com.lineyk.characterchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CharacterChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(CharacterChatApplication.class, args);
    }

}
