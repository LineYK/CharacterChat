package com.lineyk.characterchat.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class PendingRecoveryScheduler {

    private final PendingRecoveryService pendingRecoveryService;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        log.info("Application started - performing pending recovery");
        pendingRecoveryService.recoverPendingItems();
    }

    @Scheduled(fixedRate = 300000) // Run 매 5 분
    public void scheduledCleanup() {
        log.info("Scheduled pending recovery started");
        pendingRecoveryService.recoverPendingItems();
    }
        
}
