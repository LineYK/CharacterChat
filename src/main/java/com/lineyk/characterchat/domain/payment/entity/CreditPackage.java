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
public class CreditPackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int baseCredits;

    @Column(nullable = false)
    private int bonusCredits;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private int sortOrder;

    @Builder
    public CreditPackage(String name, int price, int baseCredits, int bonusCredits, int sortOrder) {
        this.name = name;
        this.price = price;
        this.baseCredits = baseCredits;
        this.bonusCredits = bonusCredits;
        this.sortOrder = sortOrder;
    }

    public int getTotalCredits() {
        return baseCredits + bonusCredits;
    }
}
