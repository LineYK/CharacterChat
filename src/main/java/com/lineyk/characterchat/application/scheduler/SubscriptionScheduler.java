package com.lineyk.characterchat.application.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.payment.entity.Subscription;
import com.lineyk.characterchat.domain.payment.service.SubscriptionService;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import com.lineyk.characterchat.global.payment.TossPaymentClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {
    
    private final SubscriptionService subscriptionService;
    private final WalletService walletService;
    private final TossPaymentClient tossPaymentClient;

    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    public void processSubscriptions() {
        LocalDate today = LocalDate.now();
        log.info("구독 스케줄러 실행 : {}", today);

        List<Subscription> expiredCancellations = subscriptionService.getExpiredCancelScheduled(today);

        for (Subscription sub : expiredCancellations) {
            log.info("만료된 구독 취소 처리: {}", sub.getId());
            sub.confirmCancel();
        }

        List<Subscription> subscriptionsToRenew = subscriptionService.getActiveSubscriptionsToRenew(today);
        for (Subscription sub : subscriptionsToRenew) {
            String newOrderId = UUID.randomUUID().toString();

            try {
                tossPaymentClient.executeBillingKey(
                    sub.getBillingKey(),
                    newOrderId,
                    sub.getUser().getId().toString(),
                    sub.getSubscriptionPlan().getMonthlyPrice(),
                    sub.getSubscriptionPlan().getName() + " 구독 갱신"
                );

                sub.renew();

                walletService.chargeCredits(
                    sub.getUser().getId(),
                    sub.getSubscriptionPlan().getInitialCredit(),
                    sub.getId()
                );

                log.info("구독 자동 갱신 완료: subscriptionId={}, orderId={}", sub.getId(), newOrderId);
            } catch (Exception e) {
                log.error("구독 갱신 실패: {}", sub.getId(), e);
                sub.failRenewal();
            }
        }
    }

}
