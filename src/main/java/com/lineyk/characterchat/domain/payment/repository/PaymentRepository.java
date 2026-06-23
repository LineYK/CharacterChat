package com.lineyk.characterchat.domain.payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lineyk.characterchat.domain.payment.entity.Payment;
import com.lineyk.characterchat.domain.user.entity.User;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findByUserOrderByCreatedAtDesc(User user);
}
