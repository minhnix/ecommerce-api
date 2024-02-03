package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.repository.custom.CustomizedProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends EntityGraphJpaRepository<Product, Long>, CustomizedProductRepository {
    @Query("select p from Product p left join fetch p.category where p.isPublished = :isPublished")
    Page<Product> findAllByPublished(Pageable pageable, @Param("isPublished") boolean isPublished);

    Optional<Product> findById(Long id, EntityGraph entityGraph);

    boolean existsByName(String name);
    @Modifying
    @Query(value = "DELETE FROM products p where p.product_id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
