package com.lineyk.characterchat.domain.payment.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionStatus {
    ACTIVE("ACTIVE", "활성"),
    CANCEL_SCHEDULED("CANCEL_SCHEDULED", "취소 예약"),
    CANCELED("CANCELED", "취소"),
    EXPIRED("EXPIRED", "만료");

    private final String key;
    private final String title;
}
