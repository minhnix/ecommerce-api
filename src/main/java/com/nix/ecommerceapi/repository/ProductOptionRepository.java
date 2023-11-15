package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.entity.ProductOption;

import java.util.List;

public interface ProductOptionRepository extends EntityGraphJpaRepository<ProductOption, Long> {
    List<ProductOption> findAllByProduct(Product product, EntityGraph entityGraph);
}
