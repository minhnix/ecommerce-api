package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.InventoryProduct;
import com.nix.ecommerceapi.repository.custom.CustomizedInventoryProductRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryProductRepository extends EntityGraphJpaRepository<InventoryProduct, Long>, CustomizedInventoryProductRepository {
    @Query(value = "delete from inventory_products where product_product_id = :id", nativeQuery = true)
    @Modifying
    void deleteByProductId(@Param("id") Long id);
}
