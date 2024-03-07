package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Permission;
import com.nix.ecommerceapi.model.request.PermissionRequest;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.model.response.PermissionResponse;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface PermissionService {
    Permission create(PermissionRequest permission);

    void update(Long id, List<String> action);

    void delete(Long id);

    PagedResponse<Permission> findAll(Pageable pageable);
    Permission findOne(Long id);

    List<Permission> findAllByRoleId(Long roleId);

    List<PermissionResponse> findByRoleIdsAndResourceName(Collection<?> roleId, String resourceName);
}
