package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Inventory;
import com.nix.ecommerceapi.repository.custom.CustomizedInventoryRepository;

public interface InventoryRepository extends EntityGraphJpaRepository<Inventory, Long>, CustomizedInventoryRepository {
}
