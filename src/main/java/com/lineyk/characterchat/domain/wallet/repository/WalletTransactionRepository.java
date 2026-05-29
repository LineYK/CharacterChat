package com.lineyk.characterchat.domain.wallet.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lineyk.characterchat.domain.wallet.entity.TransactionType;
import com.lineyk.characterchat.domain.wallet.entity.WalletTransaction;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {

    Optional<WalletTransaction> findByReferenceIdAndType(UUID referenceId, TransactionType type);
    
}