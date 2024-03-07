package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Permission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PermissionRepository extends EntityGraphJpaRepository<Permission, Long> {
    @Query("DELETE FROM Permission p WHERE p.resource.id = :resourceId")
    @Modifying
    @Transactional
    void deleteALlByResourceId(@Param("resourceId") Long resourceId);

    List<Permission> findAllByRoleId(Long roleId, EntityGraph entityGraph);

    @Query("select p from Permission p left join fetch p.resource where p.role.id in :roleId and lower(p.resource.name) = lower(:resourceName)")
    List<Permission> findByRoleIdInAndResourceNameIgnoreCase(Collection<?> roleId, String resourceName, EntityGraph entityGraph);
}
