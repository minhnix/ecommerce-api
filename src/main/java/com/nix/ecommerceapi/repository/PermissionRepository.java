package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Permission;

public interface PermissionRepository extends EntityGraphJpaRepository<Permission, Long> {
}
