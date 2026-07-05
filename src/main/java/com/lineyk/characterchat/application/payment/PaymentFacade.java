package com.lineyk.characterchat.application.payment;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.payment.dto.PaymentRequest;
import com.lineyk.characterchat.domain.payment.dto.PaymentResponse;
import com.lineyk.characterchat.domain.payment.entity.Payment;
import com.lineyk.characterchat.domain.payment.entity.PaymentMethod;
import com.lineyk.characterchat.domain.payment.entity.PaymentStatus;
import com.lineyk.characterchat.domain.payment.service.PaymentService;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;
import com.lineyk.characterchat.global.payment.TossPaymentClient;
import com.lineyk.characterchat.global.payment.dto.TossConfirmResponse;
import com.lineyk.characterchat.global.payment.dto.TossWebhookRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentFacade {
    
    private final PaymentService paymentService;
    private final WalletService walletService;
    private final TossPaymentClient tossPaymentClient;

    @Transactional
    public PaymentResponse confirmPayment(User user, PaymentRequest request) {
        Payment payment = paymentService.getPaymentByOrderId(request.orderId());

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.PAYMENT_ACCESS_DENIED);
        }

        if (payment.getAmount() != request.amount()) {
            payment.fail("금액 불일치 요청=" + request.amount() + ", 실제 금액=" + payment.getAmount());
            throw new CustomException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        TossConfirmResponse response = tossPaymentClient.confirmPayment(request.paymentKey(), request.orderId(), request.amount());

        PaymentMethod paymentMethod = mapToPaymentMethod(response);
        payment.completePayment(request.paymentKey(), paymentMethod);

        walletService.chargeCredits(
            payment.getUser().getId(), 
            payment.getCreditAmount(), 
            payment.getId()
        );

        return PaymentResponse.from(payment);
    }

    @Transactional
    public void handleWebhook(TossWebhookRequest request) {
        Payment payment = paymentService.getPaymentByOrderId(request.data().orderId());

        if (payment.getStatus() != PaymentStatus.PENDING) {
            log.info("이미 처리된 결제: orderId={}, status={}", request.data().orderId(), payment.getStatus());
            return;
        }

        if (request.data().status().equals("DONE")) {
            payment.completePayment(request.data().paymentKey(), PaymentMethod.CARD); // 우선 카드 결제만 처리
            walletService.chargeCredits(
                payment.getUser().getId(), 
                payment.getCreditAmount(), 
                payment.getId()
            );
        } else {
            payment.fail("웹훅 상태: " + request.data().status());
        }

    }
        
    private PaymentMethod mapToPaymentMethod(TossConfirmResponse response) {
        if ("간편결제".equals(response.method()) && response.easyPay() != null) {
            return switch (response.easyPay().provider()) {
                case "토스페이" -> PaymentMethod.TOSS_PAY;
                case "카카오페이" -> PaymentMethod.KAKAO_PAY;
                case "네이버페이" -> PaymentMethod.NAVER_PAY;
                default -> PaymentMethod.CARD;
            };
        };
        return PaymentMethod.CARD;
    }


    @Transactional
    public PaymentResponse refundPayment(UUID paymentId, User user) {
        Payment payment = paymentService.getPaymentById(paymentId);

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.PAYMENT_ACCESS_DENIED);
        }
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_STATUS);
        }
        if (payment.getCompletedAt().plusDays(7).isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.REFUND_PERIOD_EXPIRED);
        } 

        tossPaymentClient.cancelPayment(payment.getPaymentKey(), "사용자 환불 요청");
        walletService.deductCredits(
            payment.getUser().getId(), 
            payment.getCreditAmount(), 
            payment.getId()
        );
        payment.refund();

        return PaymentResponse.from(payment);
    }
}
