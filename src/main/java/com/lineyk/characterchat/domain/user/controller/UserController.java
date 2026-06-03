package com.lineyk.characterchat.domain.user.controller;

import com.lineyk.characterchat.application.SignupFacade;
import com.lineyk.characterchat.domain.user.dto.LoginRequest;
import com.lineyk.characterchat.domain.user.dto.LoginResponse;
import com.lineyk.characterchat.domain.user.dto.SignupRequest;
import com.lineyk.characterchat.domain.user.dto.UserResponse;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.user.service.UserService;
import com.lineyk.characterchat.global.auth.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
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
    private final SignupFacade signupApplication;

    @Operation(
        summary = "회원가입", 
        description = "새로운 사용자를 등록합니다.",
        security = {}
    )
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse response = signupApplication.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "로그인",
        description = "사용자 로그인을 합니다.",
        security = {}
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
        summary = "내 정보 조회",
        description = "현재 로그인한 사용자의 정보를 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.user();
        return ResponseEntity.ok(UserResponse.from(user));
    }
}
