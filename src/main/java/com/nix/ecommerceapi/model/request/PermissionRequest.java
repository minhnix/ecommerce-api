package com.nix.ecommerceapi.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PermissionRequest {
    @NotNull
    private Long roleId;
    @NotNull
    private Long ResourceId;
    @NotNull
    private List<String> action;
}
