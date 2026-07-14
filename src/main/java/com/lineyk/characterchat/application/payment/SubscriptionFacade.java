package com.lineyk.characterchat.application.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.payment.dto.SubscribeRequest;
import com.lineyk.characterchat.domain.payment.dto.SubscriptionResponse;
import com.lineyk.characterchat.domain.payment.entity.Subscription;
import com.lineyk.characterchat.domain.payment.entity.SubscriptionPlan;
import com.lineyk.characterchat.domain.payment.entity.SubscriptionStatus;
import com.lineyk.characterchat.domain.payment.service.SubscriptionService;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;
import com.lineyk.characterchat.global.payment.TossPaymentClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionFacade {

    private final SubscriptionService subscriptionService;
    private final WalletService walletService;
    private final TossPaymentClient tossPaymentClient;

    @Transactional
    public SubscriptionResponse subscribe(User user, SubscribeRequest request) {
        subscriptionService.validateNoSubscription(user);

        SubscriptionPlan plan = subscriptionService.getPlanById(request.planId());

        tossPaymentClient.confirmPayment(request.paymentKey(), request.orderId(), request.amount());

        Subscription subscription = subscriptionService.createSubscription(user, plan, null); // TODO: billingKey 구현

        walletService.chargeCredits(user.getId(), plan.getInitialCredit(), subscription.getId());
        return SubscriptionResponse.from(subscription);
    }
    
    @Transactional
    public SubscriptionResponse claimDailyCredits(User user) {
        Subscription subscription = subscriptionService.getActiveSubscription(user);

        if (subscription.isClaimedToday()) {
            throw new CustomException(ErrorCode.ALREADY_CLAIMED_TODAY);
        }

        subscription.claim();
        int dailyCredit = subscription.getSubscriptionPlan().getDailyCredit();
        walletService.chargeCredits(user.getId(), dailyCredit, subscription.getId());

        log.info("출석체크 완료 - userId: {}, credits: {}", user.getId(), dailyCredit);
        return SubscriptionResponse.from(subscription);
    }

    @Transactional
    public SubscriptionResponse cancelSubscription(User user) {
        Subscription subscription = subscriptionService.getActiveSubscription(user);
        
        if (subscription.getStatus() == SubscriptionStatus.CANCEL_SCHEDULED) {
            throw new CustomException(ErrorCode.SUBSCRIPTION_ALREADY_CANCELLED);
        }

        subscription.requestCancel();
        log.info("구독 해지 예약 - userId: {}, 만료일: {}", user.getId(), subscription.getCurrentPeriodEnd());

        return SubscriptionResponse.from(subscription);
    }

    public SubscriptionResponse getSubscription(User user) {
        Subscription subscription = subscriptionService.getActiveSubscription(user);
        return SubscriptionResponse.from(subscription);
    }

}
