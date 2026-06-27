package com.lineyk.characterchat.domain.payment.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lineyk.characterchat.domain.payment.entity.Payment;
import com.lineyk.characterchat.domain.payment.entity.PaymentMethod;
import com.lineyk.characterchat.domain.payment.entity.PaymentStatus;

public record PaymentResponse(
    UUID paymentId,
    String orderId,
    String packageName,
    int amount,
    int creditAmount,
    PaymentStatus status,
    PaymentMethod method,
    LocalDateTime createdAt,
    LocalDateTime completedAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getOrderId(),
            payment.getCreditPackage().getName(),
            payment.getAmount(),
            payment.getCreditAmount(),
            payment.getStatus(),
            payment.getMethod(),
            payment.getCreatedAt(),
            payment.getCompletedAt()
        );
    }
}
