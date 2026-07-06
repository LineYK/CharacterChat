package com.lineyk.characterchat.domain.payment.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int monthlyPrice;

    @Column(nullable = false)
    private int dailyCredit;

    @Column(nullable = false)
    private int initialCredit;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private int sortOrder;

    @Builder
    public SubscriptionPlan(String name, int monthlyPrice, int dailyCredit, int initialCredit, int sortOrder) {
        this.name = name;
        this.monthlyPrice = monthlyPrice;
        this.dailyCredit = dailyCredit;
        this.initialCredit = initialCredit;
        this.sortOrder = sortOrder;
    }
}
