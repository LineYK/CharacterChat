package com.lineyk.characterchat.domain.user.service;

import com.lineyk.characterchat.domain.user.dto.LoginRequest;
import com.lineyk.characterchat.domain.user.dto.LoginResponse;
import com.lineyk.characterchat.domain.user.dto.SignupRequest;
import com.lineyk.characterchat.domain.user.dto.UserResponse;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.user.repository.UserRepository;
import com.lineyk.characterchat.global.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider  jwtTokenProvider;

    @Transactional
    public UserResponse signup(SignupRequest request) {
        User savedUser = userRepository.save(request.toEntity(passwordEncoder));
        return UserResponse.from(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateToken(user.getEmail());

        return LoginResponse.of(accessToken, user);
    }
}
