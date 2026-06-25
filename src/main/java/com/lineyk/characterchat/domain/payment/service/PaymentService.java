package com.lineyk.characterchat.domain.payment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lineyk.characterchat.domain.payment.entity.CreditPackage;
import com.lineyk.characterchat.domain.payment.entity.Payment;
import com.lineyk.characterchat.domain.payment.repository.CreditPackageRepository;
import com.lineyk.characterchat.domain.payment.repository.PaymentRepository;
import com.lineyk.characterchat.domain.user.entity.User;
import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final CreditPackageRepository creditPackageRepository;

    public List<CreditPackage> getActiveCreditPackages() {
        return creditPackageRepository.findByActiveTrueOrderBySortOrderAsc();
    }

    @Transactional
    public Payment createOrder(User user, UUID packageId) {
        CreditPackage creditPackage = creditPackageRepository.findById(packageId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        String orderId = "ORDER-" + UUID.randomUUID();
        
        Payment payment = Payment.builder()
                .user(user)
                .creditPackage(creditPackage)
                .orderId(orderId)
                .amount(creditPackage.getPrice())
                .creditAmount(creditPackage.getTotalCredits())
                .build();

        return paymentRepository.save(payment);
    }

    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    public List<Payment> getPaymentsHistory(User user) {
        return paymentRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
