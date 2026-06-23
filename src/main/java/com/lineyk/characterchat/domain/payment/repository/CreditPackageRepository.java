package com.lineyk.characterchat.domain.payment.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lineyk.characterchat.domain.payment.entity.CreditPackage;

public interface CreditPackageRepository extends JpaRepository<CreditPackage, UUID> {
    List<CreditPackage> findByActiveTrueOrderBySortOrderAsc();
}
