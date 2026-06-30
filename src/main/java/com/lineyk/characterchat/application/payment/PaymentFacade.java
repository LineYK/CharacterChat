package com.lineyk.characterchat.application.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.payment.dto.PaymentRequest;
import com.lineyk.characterchat.domain.payment.dto.PaymentResponse;
import com.lineyk.characterchat.domain.payment.entity.Payment;
import com.lineyk.characterchat.domain.payment.entity.PaymentMethod;
import com.lineyk.characterchat.domain.payment.service.PaymentService;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.domain.wallet.service.WalletService;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;
import com.lineyk.characterchat.global.payment.TossPaymentClient;
import com.lineyk.characterchat.global.payment.dto.TossConfirmResponse;

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

        PaymentMethod paymentMethod = mapToPaymentMethod(response.method());
        payment.completePayment(request.paymentKey(), paymentMethod);

        walletService.chargeCredits(
            payment.getUser().getId(), 
            payment.getCreditAmount(), 
            payment.getId()
        );

        return PaymentResponse.from(payment);
    }
        
    private PaymentMethod mapToPaymentMethod(String method) {
        return switch (method) {
            case "Card" -> PaymentMethod.CARD;
            case "KakaoPay" -> PaymentMethod.KAKAO_PAY;
            case "NaverPay" -> PaymentMethod.NAVER_PAY;
            case "TossPay" -> PaymentMethod.TOSS_PAY;
            default -> throw new CustomException(ErrorCode.INVALID_PAYMENT_METHOD);
        };
    }

}
