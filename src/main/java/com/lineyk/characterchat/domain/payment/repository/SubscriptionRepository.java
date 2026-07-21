package com.lineyk.characterchat.domain.payment.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lineyk.characterchat.domain.payment.entity.Subscription;
import com.lineyk.characterchat.domain.payment.entity.SubscriptionStatus;
import com.lineyk.characterchat.domain.user.entity.User;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByUserAndStatusIn(User user, List<SubscriptionStatus> statuses);

    List<Subscription> findByStatusInAndCurrentPeriodEndLessThanEqual(List<SubscriptionStatus> statuses, LocalDate date);

    boolean existsByUserAndStatusIn(User user, List<SubscriptionStatus> statuses);
}
