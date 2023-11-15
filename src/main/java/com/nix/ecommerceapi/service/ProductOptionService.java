package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.dto.ProductOptionsDTO;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.entity.ProductOption;

import java.util.List;

public interface ProductOptionService {
    ProductOption createProductOption(Long productId, Long optionId, String value);
    void batchInsertProductOption(Long productId, Long optionId, List<String> values);
    void batchInsertProductOption(Long productId, List<ProductOptionsDTO> productOptions);
    List<ProductOption> findAllByProduct(Product product);

}
