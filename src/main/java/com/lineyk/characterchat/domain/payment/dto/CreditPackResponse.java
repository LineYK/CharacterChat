package com.lineyk.characterchat.domain.payment.dto;

import java.util.UUID;

import com.lineyk.characterchat.domain.payment.entity.CreditPackage;

public record CreditPackResponse(
    UUID id,
    String name,
    int price,
    int baseCredits,
    int bonusCredits,
    int totalCredits
) {
    public static CreditPackResponse from(CreditPackage creditPackage) {
        return new CreditPackResponse(
            creditPackage.getId(),
            creditPackage.getName(),
            creditPackage.getPrice(),
            creditPackage.getBaseCredits(),
            creditPackage.getBonusCredits(),
            creditPackage.getTotalCredits()
        );
    }
}
