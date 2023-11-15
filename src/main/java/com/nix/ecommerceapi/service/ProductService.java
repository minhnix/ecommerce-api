package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.request.ProductRequest;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.model.response.ProductDetailResponse;
import com.nix.ecommerceapi.model.response.SimpleProductResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product createProduct(ProductRequest productRequest);
    SimpleProductResponse publishProduct(Long id);
    SimpleProductResponse unPublishProduct(Long id);
    PagedResponse<SimpleProductResponse> findAllPublishProduct(Pageable pageable);
    PagedResponse<SimpleProductResponse> findAllUnPublishProduct(Pageable pageable);
    ProductDetailResponse findOneProduct(Long id, CustomUserDetails user);
    Product updateProduct(Long productId, ProductRequest productRequest);
    void deleteProduct(Long productId);
}
