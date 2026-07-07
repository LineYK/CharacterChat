package com.lineyk.characterchat.domain.payment.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lineyk.characterchat.domain.payment.entity.SubscriptionPlan;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID> {
    List<SubscriptionPlan> findByActiveTrueOrderBySortOrderAsc();
}