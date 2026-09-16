package com.lineyk.characterchat.global.payment;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.lineyk.characterchat.global.error.CustomException;
import com.lineyk.characterchat.global.error.ErrorCode;
import com.lineyk.characterchat.global.payment.dto.TossConfirmResponse;

import java.util.UUID;
import com.lineyk.characterchat.global.payment.util.TossCustomerKeyUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TossPaymentClient {
    
    private final WebClient webClient;
    private final String secretKey;

    public TossPaymentClient(
        @Value("${toss.payment.secret-key}") String secretKey,
        @Value("${toss.payment.base-url}") String baseUrl
    ) {
        this.secretKey = secretKey;

        String encodedKey = Base64.getEncoder()
            .encodeToString((secretKey + ":").getBytes());

        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedKey)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public String generateCustomerKey(UUID userId) {
        return TossCustomerKeyUtil.generateCustomerKey(userId, this.secretKey);
    }

    public TossConfirmResponse confirmPayment(String paymentKey, String orderId, int amount) {
        Map<String, Object> body = Map.of(
            "paymentKey", paymentKey,
            "orderId", orderId,
            "amount", amount
        );

        return webClient.post()
            .uri("/payments/confirm")
            .bodyValue(body)
            .retrieve()
            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                    .map(errorBody -> {
                        log.error("❌ Toss Confirm Error Response: {}", errorBody);
                        return new CustomException(ErrorCode.PAYMENT_CONFIRM_FAILED);
                    }))
            .bodyToMono(TossConfirmResponse.class)
            .block();            
    }

    public void cancelPayment(String paymentKey, String cancelReason) {
        webClient.post()
            .uri("/payments/{paymentKey}/cancel", paymentKey)
            .bodyValue(Map.of("cancelReason", cancelReason))
            .retrieve()
            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                    .map(errorBody -> new CustomException(ErrorCode.PAYMENT_CANCEL_FAILED)))
            .bodyToMono(Void.class)
            .block(); 
    }

    public TossConfirmResponse executeBillingKey(String billingKey, String orderId, String customerKey, int amount, String orderName) {
        Map<String, Object> body = Map.of(
            "customerKey", customerKey,
            "orderId", orderId,
            "amount", amount,
            "orderName", orderName
        );

        return webClient.post()
            .uri("/billing/{billingKey}", billingKey)
            .bodyValue(body)
            .retrieve()
            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                    .map(errorBody -> {
                        log.error("Toss Billing Error: {}", errorBody);
                        return new CustomException(ErrorCode.PAYMENT_CONFIRM_FAILED);
                    }))
            .bodyToMono(TossConfirmResponse.class)
            .block();
    }

    public String issueBillingKey(String authKey, String customerKey) {
        Map<String, Object> body = Map.of(
            "authKey", authKey,
            "customerKey", customerKey
        );

        return webClient.post()
            .uri("/billing/authorizations/issue")
            .bodyValue(body)
            .retrieve()
            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                    .map(errorBody -> {
                        log.error("Toss Billing Key Issue Error: {}", errorBody);
                        return new CustomException(ErrorCode.PAYMENT_BILLING_KEY_ISSUE_FAILED);
                    }))
            .bodyToMono(Map.class)
            .map(res -> (String) res.get("billingKey"))
            .block();
    }
}
