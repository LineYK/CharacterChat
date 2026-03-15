package com.lineyk.characterchat.domain.user.service;

import com.lineyk.characterchat.domain.user.dto.SignupRequest;
import com.lineyk.characterchat.domain.user.dto.UserResponse;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.user.repository.UserRepository;
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

    @Transactional
    public UserResponse signup(SignupRequest request) {
        User savedUser = userRepository.save(request.toEntity(passwordEncoder));
        return UserResponse.from(savedUser);
    }
}
