package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.OrderDetail;

public interface OrderDetailRepository extends EntityGraphJpaRepository<OrderDetail, Long> {
}
