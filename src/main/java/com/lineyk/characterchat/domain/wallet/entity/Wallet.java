package com.lineyk.characterchat.domain.wallet.entity;

import java.util.UUID;

import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wallets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet {
    
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private long credits;

    @Builder
    public Wallet(User user, long initialBalance) {
        this.user = user;
        this.credits = initialBalance;
    }

    public void charge(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        this.credits += amount;
    }

    public void spend(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");
        }
        if (this.credits < amount) {
            throw new CustomException(ErrorCode.INSUFFICIENT_CREDITS);
        }
        this.credits -= amount;
    }
}
