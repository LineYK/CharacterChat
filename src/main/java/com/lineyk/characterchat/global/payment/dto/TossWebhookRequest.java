package com.lineyk.characterchat.global.payment.dto;

public record TossWebhookRequest(
    String eventType, // PAYMENT_STATUS_CHANGED
    String createdAt,
    TossWebhookData data
) {
    public record TossWebhookData(
        String paymentKey,
        String orderId,
        String status,
        String method
    ) {}
}