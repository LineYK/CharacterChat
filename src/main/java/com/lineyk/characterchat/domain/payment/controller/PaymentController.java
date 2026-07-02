package com.lineyk.characterchat.domain.payment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lineyk.characterchat.application.payment.PaymentFacade;
import com.lineyk.characterchat.domain.payment.dto.CreditPackResponse;
import com.lineyk.characterchat.domain.payment.dto.OrderRequest;
import com.lineyk.characterchat.domain.payment.dto.PaymentRequest;
import com.lineyk.characterchat.domain.payment.dto.PaymentResponse;
import com.lineyk.characterchat.domain.payment.entity.Payment;
import com.lineyk.characterchat.domain.payment.service.PaymentService;
import com.lineyk.characterchat.global.auth.security.CustomUserDetails;
import com.lineyk.characterchat.global.payment.dto.TossWebhookRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    private final PaymentFacade paymentFacade;

    @GetMapping("/packages")
    public ResponseEntity<?> getPackages() {
        List<CreditPackResponse> packages = paymentService.getActiveCreditPackages()
            .stream()
            .map(CreditPackResponse::from)
            .toList();
        return ResponseEntity.ok(packages);
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody OrderRequest request
    ) {
        Payment payment = paymentService.createOrder(userDetails.user(), request.packageId());
        PaymentResponse response = PaymentResponse.from(payment);
       
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody PaymentRequest request
    ) {
        PaymentResponse response = paymentFacade.confirmPayment(userDetails.user(), request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<PaymentResponse> history = paymentService.getPaymentsHistory(userDetails.user())
            .stream()
            .map(PaymentResponse::from)
            .toList();
        return ResponseEntity.ok(history);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody TossWebhookRequest request) {
        paymentFacade.handleWebhook(request);
        return ResponseEntity.ok().build();
    }
    
    
}
