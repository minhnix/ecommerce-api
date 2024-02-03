package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.InventoryProduct;
import com.nix.ecommerceapi.model.entity.Product;

public interface InventoryProductService {
    InventoryProduct createInventoryProduct(Product product, Long stock);
    InventoryProduct updateInventoryProduct(Long productId, Long stock, Long totalSold, String location);
}
