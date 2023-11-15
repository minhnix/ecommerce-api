package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.nix.ecommerceapi.model.entity.User;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends EntityGraphJpaRepository<User, Long>, EntityGraphJpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmail(String email, EntityGraph entityGraph);
    Boolean existsByEmail(String email);

}
