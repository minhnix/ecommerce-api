package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Category;
import com.nix.ecommerceapi.model.response.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Category createCategory(Category category);
    Category updateCategory(Long id, String newName);
    Category findCategoryById(Long id);
    void deleteCategory(Long id);
    PagedResponse<Category> findAll(Pageable pageable);
}
