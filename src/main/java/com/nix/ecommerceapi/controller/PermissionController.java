package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.model.constants.PageConstants;
import com.nix.ecommerceapi.model.entity.Permission;
import com.nix.ecommerceapi.model.request.PermissionRequest;
import com.nix.ecommerceapi.model.response.ApiResponse;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.service.PermissionService;
import com.nix.ecommerceapi.utils.PageableUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/permissions")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    public PagedResponse<?> getAll(
            @RequestParam(value = "page", defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = PageConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDir", required = false) String sortDir
    ) {
        Pageable pageable = PageableUtils.getPageable(page, size, sortBy, sortDir);
        return permissionService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ApiResponse findOne(@PathVariable("id") Long id) {
        return new ApiResponse(permissionService.findOne(id), "Permission", HttpStatus.OK.value());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Permission createCategory(@RequestBody @Valid PermissionRequest permission) {
        return permissionService.create(permission);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable("id") Long id, @RequestBody PermissionRequest permission) {
        permissionService.update(id, permission.getAction());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") Long id) {
        permissionService.delete(id);
    }

}
