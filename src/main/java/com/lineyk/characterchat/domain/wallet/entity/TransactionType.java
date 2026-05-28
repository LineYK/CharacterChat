package com.lineyk.characterchat.domain.wallet.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    CHARGE("wallet_charge", "크레딧 충전"),
    USE("wallet_use", "크레딧 사용"),
    WELCOME_BONUS("wallet_welcome_bonus", "웰컴 보너스"),
    REFUND("wallet_refund", "크레딧 환불");

    private final String key;
    private final String description;

}
