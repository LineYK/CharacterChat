package com.lineyk.characterchat.domain.payment.dto;

public record PaymentRequest(
    String paymentKey,
    String orderId,
    int amount
) {
    
}
