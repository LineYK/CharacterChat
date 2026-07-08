package com.lineyk.characterchat.domain.payment.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.lineyk.characterchat.domain.payment.entity.Subscription;
import com.lineyk.characterchat.domain.payment.entity.SubscriptionStatus;

public record SubscriptionResponse(
    UUID id,
    String planName,
    SubscriptionStatus status,
    int dailyCredit,
    boolean claimedToday,
    LocalDate currentPeriodStart,
    LocalDate currentPeriodEnd,
    LocalDate cancelRequestedAt
) {
    public static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(
            subscription.getId(),
            subscription.getSubscriptionPlan().getName(),
            subscription.getStatus(),
            subscription.getSubscriptionPlan().getDailyCredit(),
            subscription.isClaimedToday(),
            subscription.getCurrentPeriodStart(),
            subscription.getCurrentPeriodEnd(),
            subscription.getCanceledRequestedAt()
        );
    }
}
