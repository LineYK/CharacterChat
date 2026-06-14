package com.lineyk.characterchat.application.scheduler;

import org.springframework.stereotype.Service;

import com.lineyk.characterchat.domain.chat.service.ChatService;
import com.lineyk.characterchat.domain.wallet.service.WalletService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PendingRecoveryService {
    
    private final ChatService chatService;
    private final WalletService walletService;

    @Transactional
    public void recoverPendingItems() {
        chatService.recoverPendingChats();
        walletService.recoverPendingTransactions();
    }
}
