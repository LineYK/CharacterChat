package com.lineyk.characterchat.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.user.dto.SignupRequest;
import com.lineyk.characterchat.domain.user.dto.UserResponse;
import com.lineyk.characterchat.domain.user.service.UserService;
import com.lineyk.characterchat.domain.wallet.service.WalletService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupFacade {
    
    private final UserService userService;
    private final WalletService walletService;

    @Transactional
    public UserResponse signup(SignupRequest request) {
 
        UserResponse userResponse = userService.signup(request);
        walletService.createWallet(userResponse.uuid());
        return userResponse;

    }

}
