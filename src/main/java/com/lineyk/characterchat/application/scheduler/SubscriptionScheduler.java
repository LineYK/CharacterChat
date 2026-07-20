package com.lineyk.characterchat.application.scheduler;

import java.time.LocalDate;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.lineyk.characterchat.domain.payment.entity.Subscription;
import com.lineyk.characterchat.domain.payment.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {
    
    private final SubscriptionService subscriptionService;
    private final SubscriptionRenewalProcessor processor;
    private final SubscriptionRenewalTxService txService;

    @Scheduled(cron = "0 1 0 * * *")
    public void processSubscriptions() {
        LocalDate today = LocalDate.now();
        log.info("구독 스케줄러 실행 : {}", today);

        List<Subscription> expiredCancellations = subscriptionService.getExpiredCancelScheduled(today);

        txService.processExpiredCancellation(expiredCancellations);

        List<Subscription> subscriptionsToRenew = subscriptionService.getActiveSubscriptionsToRenew(today);
        for (Subscription sub : subscriptionsToRenew) {
            try {
                processor.renewSingle(sub);
            } catch (Exception e) {
                log.error("Failed to renew subscription: {}", sub.getId(), e);
            }
        }
    }

}
