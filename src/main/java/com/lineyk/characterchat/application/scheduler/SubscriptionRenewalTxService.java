package com.lineyk.characterchat.application.scheduler;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.payment.entity.Subscription;
import com.lineyk.characterchat.domain.wallet.service.WalletService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionRenewalTxService {
    
    private final WalletService walletService;

    @Transactional
    public void completeRenewal(Subscription sub) {
        sub.renew();
        walletService.chargeCredits(
            sub.getUser().getId(),
            sub.getSubscriptionPlan().getInitialCredit(),
            sub.getId()
        );
    }

    @Transactional
    public void failRenewal(Subscription sub) {
        sub.failRenewal();
    }

    @Transactional
    public void processExpiredCancellation(List<Subscription> subscriptions) {
        subscriptions.forEach(Subscription::confirmCancel);
    }
}
