package com.lineyk.characterchat.global.payment.dto;

public record TossConfirmResponse(
    String paymentKey,
    String orderId,
    String status,
    int totalAmount,
    String method,
    EasyPay easyPay
) {
    public record EasyPay(
        String provider
    ) {}
}
