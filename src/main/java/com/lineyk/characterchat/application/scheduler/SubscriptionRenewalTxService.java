package com.lineyk.characterchat.application.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.payment.entity.Subscription;
import com.lineyk.characterchat.domain.payment.service.SubscriptionService;
import com.lineyk.characterchat.domain.wallet.service.WalletService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionRenewalTxService {
    
    private final WalletService walletService;
    private final SubscriptionService subscriptionService;

    @Transactional
    public void completeRenewal(UUID subId) {
        Subscription sub = subscriptionService.getSubscriptionById(subId);
        sub.renew();
        walletService.chargeCredits(
            sub.getUser().getId(),
            sub.getSubscriptionPlan().getInitialCredit(),
            sub.getId()
        );
    }

    @Transactional
    public void failRenewal(UUID subId) {
        Subscription sub = subscriptionService.getSubscriptionById(subId);
        sub.failRenewal();
    }

    @Transactional
    public void processExpiredCancellation(LocalDate today) {
        List<Subscription> expiredCancellations = subscriptionService.getExpiredCancelScheduled(today);
        expiredCancellations.forEach(sub -> {
            sub.confirmCancel();
            log.info("구독 만료 처리: subscriptionId={}", sub.getId());
        });
    }
}
