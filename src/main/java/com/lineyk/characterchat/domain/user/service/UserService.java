package com.lineyk.characterchat.domain.user.service;

import com.lineyk.characterchat.domain.user.dto.LoginRequest;
import com.lineyk.characterchat.domain.user.dto.LoginResponse;
import com.lineyk.characterchat.domain.user.dto.SignupRequest;
import com.lineyk.characterchat.domain.user.dto.UserResponse;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.user.repository.UserRepository;
import com.lineyk.characterchat.global.auth.jwt.JwtTokenProvider;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;
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
        validDuplicatedEmail(request.email());
        User savedUser = userRepository.save(request.toEntity(passwordEncoder));
        return UserResponse.from(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.generateToken(user.getEmail());

        return LoginResponse.of(accessToken, user);
    }

    public void validDuplicatedEmail(String email) {
        boolean isDuplicateEmail = userRepository.existsByEmail(email);
        if (isDuplicateEmail) throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
    }
}
