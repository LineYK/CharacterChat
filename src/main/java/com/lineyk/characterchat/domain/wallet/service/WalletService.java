package com.lineyk.characterchat.domain.wallet.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.user.repository.UserRepository;
import com.lineyk.characterchat.domain.wallet.entity.TransactionType;
import com.lineyk.characterchat.domain.wallet.entity.TransactionsStatus;
import com.lineyk.characterchat.domain.wallet.entity.Wallet;
import com.lineyk.characterchat.domain.wallet.entity.WalletTransaction;
import com.lineyk.characterchat.domain.wallet.repository.WalletRepository;
import com.lineyk.characterchat.domain.wallet.repository.WalletTransactionRepository;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletService {
    
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createWallet(UUID userId) {
        User userProxy = userRepository.getReferenceById(userId);

        Wallet wallet = Wallet.builder()
                .user(userProxy)
                .initialBalance(10000)
                .build();
        walletRepository.save(wallet);

        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(wallet)
                .amount(10000)
                .type(TransactionType.WELCOME_BONUS)
                .referenceId(null)
                .status(TransactionsStatus.SUCCESS)
                .build();
        transactionRepository.save(transaction);
    }

    @Transactional
    public void spendCredits(User user, long amount, UUID referenceId) {
        Wallet wallet = walletRepository.findByUserIdWithLock(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.WALLET_NOT_FOUND));

        wallet.spend(amount);

        WalletTransaction tx = WalletTransaction.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.USE)
                .referenceId(referenceId)
                .status(TransactionsStatus.SUCCESS)
                .build();
        transactionRepository.save(tx);
    }

    @Transactional
    public void chargeCredits(User user, long amount, UUID referenceId) {
        Wallet wallet = walletRepository.findByUserIdWithLock(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.WALLET_NOT_FOUND));

        wallet.charge(amount);

        WalletTransaction tx = WalletTransaction.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.CHARGE)
                .referenceId(referenceId)
                .status(TransactionsStatus.SUCCESS)
                .build();
        transactionRepository.save(tx);
    }

    @Transactional
    public void refundCredits(User user, long amount, UUID referenceId) {
        Wallet wallet = walletRepository.findByUserIdWithLock(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.WALLET_NOT_FOUND));

        wallet.charge(amount);

        WalletTransaction tx = WalletTransaction.builder()
                .wallet(wallet)
                .amount(amount)
                .type(TransactionType.REFUND)
                .referenceId(referenceId)
                .status(TransactionsStatus.SUCCESS)
                .build();
        transactionRepository.save(tx);

        WalletTransaction originalTx = transactionRepository.findByReferenceIdAndType(referenceId, TransactionType.USE)
                .orElseThrow(() -> new CustomException(ErrorCode.TRANSACTION_NOT_FOUND));
        originalTx.cancel();
    }
}
