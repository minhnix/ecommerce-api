package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.model.constants.PageConstants;
import com.nix.ecommerceapi.model.entity.Resource;
import com.nix.ecommerceapi.model.response.ApiResponse;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.service.ResourceService;
import com.nix.ecommerceapi.utils.PageableUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resources")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @GetMapping
    public PagedResponse<?> getAll(
            @RequestParam(value = "page", defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = PageConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDir", required = false) String sortDir
    ) {
        Pageable pageable = PageableUtils.getPageable(page, size, sortBy, sortDir);
        return resourceService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ApiResponse findOne(@PathVariable("id") Long id) {
        return new ApiResponse(resourceService.findById(id), "Resource", HttpStatus.OK.value());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resource createCategory(@RequestBody @Valid Resource resource) {
        return resourceService.create(resource);
    }

    @PutMapping("/{id}")
    public Resource updateCategory(@PathVariable("id") Long id, @RequestBody @Valid Resource resource) {
        resource.setId(id);
        return resourceService.update(resource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") Long id) {
        resourceService.delete(id);
    }

}
