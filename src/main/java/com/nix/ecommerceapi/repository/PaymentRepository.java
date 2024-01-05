package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends EntityGraphJpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId, EntityGraph entityGraph);
}
