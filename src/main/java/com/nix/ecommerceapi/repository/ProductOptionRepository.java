package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.entity.ProductOption;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOptionRepository extends EntityGraphJpaRepository<ProductOption, Long> {
    List<ProductOption> findAllByProduct(Product product, EntityGraph entityGraph);
    @Modifying
    @Query(value = "delete from product_options m where m.product_id = :productId", nativeQuery = true)
    void deleteAllByProductId(@Param("productId") Long productId);
}
