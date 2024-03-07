package com.nix.ecommerceapi.security.permission;

import com.nix.ecommerceapi.model.entity.Role;
import com.nix.ecommerceapi.model.response.PermissionResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RolePermissionEvaluator implements PermissionEvaluator {
    private final PermissionService permissionService;

    public RolePermissionEvaluator(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    /*
     * @param targetDomainObject The resource needs to be checked
     * @param permission The permission that needs to be checked.
     */
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication != null && targetDomainObject instanceof String resource && permission instanceof String action) {

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            List<PermissionResponse> permissions = permissionService.findByRoleIdsAndResourceName(
                    customUserDetails.getRole().stream().map(Role::getId).collect(Collectors.toList()),
                    resource);
            return permissions.stream().anyMatch(p ->
                    p.getAction().stream().anyMatch(a -> a.equalsIgnoreCase(action))
            );
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new UnsupportedOperationException("Not supported by this PermissionEvaluator: " + this.getClass().getName());
    }
}
