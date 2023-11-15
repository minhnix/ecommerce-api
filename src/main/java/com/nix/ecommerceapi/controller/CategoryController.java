package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.model.entity.Category;
import com.nix.ecommerceapi.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody @Valid Category category) {
        return categoryService.createCategory(category);
    }
}
