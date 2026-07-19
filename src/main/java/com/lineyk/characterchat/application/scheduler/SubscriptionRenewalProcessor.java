package com.lineyk.characterchat.application.scheduler;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.payment.entity.Subscription;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import com.lineyk.characterchat.global.payment.TossPaymentClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionRenewalProcessor {
    
    private final WalletService walletService;
    private final TossPaymentClient tossPaymentClient;

    public void renewSingle(Subscription sub) {
        String newOrderId = UUID.randomUUID().toString();

        try {
            tossPaymentClient.executeBillingKey(
                sub.getBillingKey(),
                newOrderId,
                sub.getUser().getId().toString(),
                sub.getSubscriptionPlan().getMonthlyPrice(),
                sub.getSubscriptionPlan().getName() + " 구독 갱신"
            );
        } catch (Exception e) {
            log.error("구독 갱신 결제 실패 : subscriptionId={}, userId={}", sub.getId(), sub.getUser().getId(), e);
            applyRenewalFailure(sub);
        }

        try {
            applyRenewalSuccess(sub);
        } catch (Exception e) {
            // TODO: handle exception
            applyRenewalFailure(sub);
        }
    }

    @Transactional
    public void applyRenewalSuccess(Subscription sub) {
        sub.renew();
        walletService.chargeCredits(
            sub.getUser().getId(),
            sub.getSubscriptionPlan().getInitialCredit(),
            sub.getId()
        );
    }

    @Transactional
    public void applyRenewalFailure(Subscription sub) {
        sub.failRenewal();
    }

    public void processExpiredCancellation(List<Subscription> subscriptions) {
        subscriptions.forEach(Subscription::confirmCancel);
    }

}
