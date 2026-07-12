package com.lineyk.characterchat.domain.payment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lineyk.characterchat.application.payment.SubscriptionFacade;
import com.lineyk.characterchat.domain.payment.dto.SubscribeRequest;
import com.lineyk.characterchat.domain.payment.dto.SubscriptionPlanResponse;
import com.lineyk.characterchat.domain.payment.dto.SubscriptionResponse;
import com.lineyk.characterchat.domain.payment.service.SubscriptionService;
import com.lineyk.characterchat.global.auth.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    
    private final SubscriptionFacade subscriptionFacade;
    private final SubscriptionService subscriptionService;

    @GetMapping("/plans")
    public ResponseEntity<?> getPlans() {
        List<SubscriptionPlanResponse> plans = subscriptionService.getActiveSubscriptionPlans();

        return ResponseEntity.ok(plans);
    }
    
    @PostMapping
    public ResponseEntity<?> subscribe(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody SubscribeRequest request
    ) {
        SubscriptionResponse response = subscriptionFacade.subscribe(userDetails.user(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    
}
