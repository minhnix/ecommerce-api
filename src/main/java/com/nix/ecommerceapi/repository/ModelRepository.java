package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Model;

import java.util.List;

public interface ModelRepository extends EntityGraphJpaRepository<Model, Long> {
    List<Model> findAllByProductId(Long productId, EntityGraph entityGraph);
}
