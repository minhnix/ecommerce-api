package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends EntityGraphJpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndModelId(Long userId, Long modelId);
    void deleteByUserId(Long userId);
    int countByUserId(Long userId);
    List<Cart> findAllByUserId(Long userId, EntityGraph entityGraph);
}
