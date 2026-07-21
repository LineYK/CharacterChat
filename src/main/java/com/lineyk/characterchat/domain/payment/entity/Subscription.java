package com.lineyk.characterchat.domain.payment.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.lineyk.characterchat.domain.user.entity.User;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Subscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    private String billingKey; // Toss Billing Key

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(nullable = false)
    private int failedCount; // 결제 실패 횟수

    @Column(nullable = false)
    private LocalDate currentPeriodStart;

    @Column(nullable = false)
    private LocalDate currentPeriodEnd;

    private LocalDate canceledRequestedAt; // 해지 요청일

    private LocalDate lastClaimedAt; // 마지막으로 크레딧을 지급받은 시점

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;


    @Builder
    public Subscription(User user, SubscriptionPlan subscriptionPlan, String billingKey) {
        this.user = user;
        this.subscriptionPlan = subscriptionPlan;
        this.billingKey = billingKey;
        this.status = SubscriptionStatus.ACTIVE;
        this.currentPeriodStart = LocalDate.now();
        this.currentPeriodEnd = currentPeriodStart.plusMonths(1);
    }

    public void claim() {
        this.lastClaimedAt = LocalDate.now();
    }

    public boolean isClaimedToday() {
        return lastClaimedAt != null && lastClaimedAt.equals(LocalDate.now());
    }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE || this.status == SubscriptionStatus.CANCEL_SCHEDULED;
    }

    public void requestCancel() {
        this.status = SubscriptionStatus.CANCEL_SCHEDULED;
        this.canceledRequestedAt = LocalDate.now();
    }

    public void confirmCancel() {
        this.status = SubscriptionStatus.CANCELED;
    }

    public void renew() {
        this.status = SubscriptionStatus.ACTIVE;
        this.failedCount = 0;
        this.currentPeriodStart = this.currentPeriodEnd;
        this.currentPeriodEnd = this.currentPeriodEnd.plusMonths(1);
        this.lastClaimedAt = null;
    }

    public void failRenewal() {
        this.failedCount++;
        if (this.failedCount >= 3) {
            this.status = SubscriptionStatus.CANCELED;
        } else {
            this.status = SubscriptionStatus.SUSPENDED;
        }
    }
}
