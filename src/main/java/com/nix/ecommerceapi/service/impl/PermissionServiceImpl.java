package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.mapper.PermissionMapper;
import com.nix.ecommerceapi.model.entity.Permission;
import com.nix.ecommerceapi.model.entity.PermissionEntityGraph;
import com.nix.ecommerceapi.model.entity.Resource;
import com.nix.ecommerceapi.model.entity.Role;
import com.nix.ecommerceapi.model.request.PermissionRequest;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.model.response.PermissionResponse;
import com.nix.ecommerceapi.repository.PermissionRepository;
import com.nix.ecommerceapi.repository.ResourceRepository;
import com.nix.ecommerceapi.repository.RoleRepository;
import com.nix.ecommerceapi.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ResourceRepository resourceRepository;

    @Override
    @Transactional
    @CacheEvict(value = "permission", allEntries = true)
    public Permission create(PermissionRequest permission) {
        Role role = roleRepository.findById(permission.getRoleId())
                .orElseThrow(() -> new NotFoundException("Role not found"));
        Resource resource = resourceRepository.findById(permission.getResourceId())
                .orElseThrow(() -> new NotFoundException("Resource not found"));
        Permission permission1 = new Permission();
        permission1.setAction(permission.getAction());
        permission1.setRole(role);
        permission1.setResource(resource);
        return permissionRepository.save(permission1);
    }

    @Override
    @Transactional
    @CacheEvict(value = "permission", allEntries = true)
    public void update(Long id, List<String> action) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Permission not found"));
        permission.setAction(action);
        permissionRepository.save(permission);
    }

    @Override
    @Transactional
    @CacheEvict(value = "permission", allEntries = true)
    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public PagedResponse<Permission> findAll(Pageable pageable) {
        return new PagedResponse<>(permissionRepository.findAll(pageable,
                PermissionEntityGraph.____().role().____.resource().____.____()
        ));
    }

    @Override
    public Permission findOne(Long id) {
        return permissionRepository.findById(id, PermissionEntityGraph.____().role().____.resource().____.____())
                .orElseThrow(() -> new NotFoundException("Permission not found"));
    }

    @Override
    public List<Permission> findAllByRoleId(Long roleId) {
        return permissionRepository.findAllByRoleId(roleId, PermissionEntityGraph.____().resource().____.____());
    }

    @Override
    @Cacheable(value = "permission", key = "#roleId.toString() + '_' + #resourceName")
    public List<PermissionResponse> findByRoleIdsAndResourceName(Collection<?> roleId, String resourceName) {
        List<Permission> permissions =  permissionRepository.findByRoleIdInAndResourceNameIgnoreCase(roleId, resourceName, PermissionEntityGraph.NOOP);
        return permissions.stream().map(PermissionMapper.INSTANCE::toPermissionResponse).collect(Collectors.toList());
    }

}
