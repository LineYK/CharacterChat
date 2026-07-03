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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TossPaymentClient {
    
    private final WebClient webClient;

    public TossPaymentClient(
        @Value("${toss.payment.secret-key}") String secretKey,
        @Value("${toss.payment.base-url}") String baseUrl
    ) {

        String encodedKey = Base64.getEncoder()
            .encodeToString((secretKey + ":").getBytes());

        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedKey)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
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
                    .map(errorBody -> new CustomException(ErrorCode.PAYMENT_CONFIRM_FAILED)))
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
}
