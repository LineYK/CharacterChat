package com.lineyk.characterchat.domain.wallet.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "reference_id", nullable = true)
    private UUID referenceId; // 관련된 엔티티의 ID (예: 충전이면 결제 ID, 사용이면 사용한 chat ID 등)

    @Enumerated(EnumType.STRING)
    private TransactionsStatus status; // 트랜잭션 상태 (성공, 실패 등)

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Builder
    public WalletTransaction(Wallet wallet, long amount, TransactionType type, UUID referenceId, TransactionsStatus status) {
        this.wallet = wallet;
        this.amount = amount;
        this.type = type;
        this.referenceId = referenceId;
        this.status = status;
    }

    public void cancel() {
        if (this.status == TransactionsStatus.SUCCESS) {
            this.status = TransactionsStatus.CANCELED;
        }
    }
}
