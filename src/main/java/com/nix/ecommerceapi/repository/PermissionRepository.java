package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Permission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PermissionRepository extends EntityGraphJpaRepository<Permission, Long> {
    @Query("DELETE FROM Permission p WHERE p.resource.id = :resourceId")
    @Modifying
    @Transactional
    void deleteALlByResourceId(@Param("resourceId") Long resourceId);
}
