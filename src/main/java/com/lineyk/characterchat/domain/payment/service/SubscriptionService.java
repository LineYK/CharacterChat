package com.lineyk.characterchat.domain.payment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.payment.dto.SubscriptionPlanResponse;
import com.lineyk.characterchat.domain.payment.dto.SubscriptionResponse;
import com.lineyk.characterchat.domain.payment.entity.Subscription;
import com.lineyk.characterchat.domain.payment.entity.SubscriptionPlan;
import com.lineyk.characterchat.domain.payment.entity.SubscriptionStatus;
import com.lineyk.characterchat.domain.payment.repository.SubscriptionPlanRepository;
import com.lineyk.characterchat.domain.payment.repository.SubscriptionRepository;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {
    
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    public List<SubscriptionPlanResponse> getActiveSubscriptionPlans() {
        return subscriptionPlanRepository.findByActiveTrueOrderBySortOrderAsc().stream()
            .map(SubscriptionPlanResponse::from)
            .toList();
    }

    public SubscriptionPlanResponse getPlanById(UUID planId) {
        return subscriptionPlanRepository.findById(planId)
            .map(SubscriptionPlanResponse::from)
            .orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));
    }

    public SubscriptionResponse getActiveSubscription(User user) {
        return subscriptionRepository.findByUserAndStatusIn(user, List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.CANCEL_SCHEDULED))
            .map(SubscriptionResponse::from)
            .orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    public void validateNoSubscription(User user) {
        if (subscriptionRepository.existsByUserAndStatusIn(user, List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.CANCEL_SCHEDULED))) {
            throw new CustomException(ErrorCode.SUBSCRIPTION_ALREADY_EXISTS);
        }
    }

    @Transactional
    public SubscriptionResponse createSubscription(User user, SubscriptionPlan plan, String billingKey) {
        Subscription subscription = Subscription.builder()
            .user(user)
            .subscriptionPlan(plan)
            .billingKey(billingKey)
            .build();

        subscriptionRepository.save(subscription);
        return SubscriptionResponse.from(subscription);
    }

    public List<SubscriptionResponse> getExpiredCancelScheduled(LocalDate date) {
        return subscriptionRepository.findByStatusAndCurrentPeriodEndBefore(SubscriptionStatus.CANCEL_SCHEDULED, date)
            .stream()
            .map(SubscriptionResponse::from)
            .toList();
    }

    public List<SubscriptionResponse> getActiveSubscriptionsToRenew(LocalDate date) {
        return subscriptionRepository.findByStatusAndCurrentPeriodEndBefore(SubscriptionStatus.ACTIVE, date)
            .stream()
            .map(SubscriptionResponse::from)
            .toList();
    }

}
