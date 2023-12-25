package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.entity.Category;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.repository.CategoryRepository;
import com.nix.ecommerceapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        log.info("create category");
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, String newName) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id " + id));
        category.setName(newName);
        return categoryRepository.save(category);
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id " + id));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public PagedResponse<Category> findAll(Pageable pageable) {
        return new PagedResponse<>(categoryRepository.findAll(pageable));
    }
}
