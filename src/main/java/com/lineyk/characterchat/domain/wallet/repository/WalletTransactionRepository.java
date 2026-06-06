package com.lineyk.characterchat.domain.wallet.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lineyk.characterchat.domain.wallet.entity.TransactionType;
import com.lineyk.characterchat.domain.wallet.entity.WalletTransaction;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {

    Optional<WalletTransaction> findByReferenceIdAndType(UUID referenceId, TransactionType type);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM WalletTransaction t WHERE t.wallet.id = :walletId AND t.type = :type AND t.status = 'PENDING'")
    Long sumPendingAmountByWalletIdAndType(UUID walletId, TransactionType type);
    
}