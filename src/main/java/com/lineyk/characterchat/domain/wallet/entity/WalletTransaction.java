package com.lineyk.characterchat.domain.wallet.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wallet_transactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class WalletTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private TransactionType type;

    @CreatedBy
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Builder
    public WalletTransaction(Wallet wallet, long amount, TransactionType type) {
        this.wallet = wallet;
        this.amount = amount;
        this.type = type;
    }
}
