package com.lineyk.characterchat.domain.user.controller;

import com.lineyk.characterchat.domain.user.dto.LoginRequest;
import com.lineyk.characterchat.domain.user.dto.LoginResponse;
import com.lineyk.characterchat.domain.user.dto.SignupRequest;
import com.lineyk.characterchat.domain.user.dto.UserResponse;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.user.service.UserService;
import com.lineyk.characterchat.global.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.user();
        return ResponseEntity.ok(UserResponse.from(user));
    }
}
