package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Model;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends EntityGraphJpaRepository<Model, Long> {
    List<Model> findAllByProductId(Long productId, EntityGraph entityGraph);

    @Query(value = "SELECT m FROM Model m WHERE m.id = ?1")
    Optional<Model> findById(Long id);

    @Modifying
    @Query(value = "delete from Model m where m.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}
