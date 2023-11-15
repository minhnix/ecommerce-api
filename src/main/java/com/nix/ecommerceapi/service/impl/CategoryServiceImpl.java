package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.model.entity.Category;
import com.nix.ecommerceapi.repository.CategoryRepository;
import com.nix.ecommerceapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
