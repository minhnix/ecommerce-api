package com.nix.ecommerceapi.model.response;

import com.nix.ecommerceapi.model.entity.Resource;
import com.nix.ecommerceapi.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse implements Serializable {
    private static final Long serialVersionUID = 1L;
    private Long id;
    private Role role;
    private Resource resource;
    private List<String> action;
}
