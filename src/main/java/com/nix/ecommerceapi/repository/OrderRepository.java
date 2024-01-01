package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Order;

import java.util.Optional;

public interface OrderRepository extends EntityGraphJpaRepository<Order, Long> {
    Optional<Order> findById(Long id, EntityGraph entityGraph);
}
