package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Role;
import com.nix.ecommerceapi.model.enumuration.RoleName;

import java.util.Optional;

public interface RoleRepository extends EntityGraphJpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
