package com.lineyk.characterchat.global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<?> getHealth() {
        return ResponseEntity.ok("ChatCharacter 서비스 잘 동작 중!");
    }

}
