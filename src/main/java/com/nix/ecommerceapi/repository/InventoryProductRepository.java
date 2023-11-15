package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.InventoryProduct;

public interface InventoryProductRepository extends EntityGraphJpaRepository<InventoryProduct, Long> {
}
