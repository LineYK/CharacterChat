package com.lineyk.characterchat.domain.wallet.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.lineyk.characterchat.domain.user.event.UserSignedUpEvent;
import com.lineyk.characterchat.domain.wallet.service.WalletService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WalletEventListener {
    
    private final WalletService walletService;
    
    @EventListener
    public void handleUserSignUpEvent(UserSignedUpEvent event) {
        walletService.createWallet(event.userId());
    }
}
