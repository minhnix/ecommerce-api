package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Inventory;

public interface InventoryRepository extends EntityGraphJpaRepository<Inventory, Long> {
}
