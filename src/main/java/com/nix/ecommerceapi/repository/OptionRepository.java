package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Option;

public interface OptionRepository extends EntityGraphJpaRepository<Option, Long> {
    boolean existsByName(String name);
}
