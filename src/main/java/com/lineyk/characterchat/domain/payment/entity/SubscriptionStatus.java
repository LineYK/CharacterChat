package com.lineyk.characterchat.domain.payment.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionStatus {
    ACTIVE("ACTIVE", "활성"),
    CANCEL_SCHEDULED("CANCEL_SCHEDULED", "취소 예약"),
    CANCELED("CANCELED", "취소"),
    SUSPENDED("SUSPENDED", "결제 실패/정지"),
    EXPIRED("EXPIRED", "만료");

    private final String key;
    private final String title;
}
