package com.lineyk.characterchat.domain.payment.dto;

import java.util.UUID;

import com.lineyk.characterchat.domain.payment.entity.SubscriptionPlan;

public record SubscriptionPlanResponse(
    UUID id,
    String name,
    int monthlyPrice,
    int dailyCredit,
    int initialCredit
) {
    public static SubscriptionPlanResponse from(SubscriptionPlan plan) {
        return new SubscriptionPlanResponse(
            plan.getId(),
            plan.getName(),
            plan.getMonthlyPrice(),
            plan.getDailyCredit(),
            plan.getInitialCredit()
        );
    }
}
