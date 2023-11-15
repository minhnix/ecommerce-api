package com.nix.ecommerceapi.repository.custom;

import com.nix.ecommerceapi.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizedProductRepository {
    Page<Product> findAllByPublished(Pageable pageable, boolean isPublished);
}
