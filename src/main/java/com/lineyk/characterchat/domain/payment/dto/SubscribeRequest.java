package com.lineyk.characterchat.domain.payment.dto;

import java.util.UUID;

public record SubscribeRequest(
    UUID planId,
    String paymentKey, // 첫 달 결제 paymentKey
    String orderId,
    int amount
) {

}
