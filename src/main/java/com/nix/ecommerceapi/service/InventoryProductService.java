package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.InventoryProduct;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.request.ProductRequest;

public interface InventoryProductService {
    InventoryProduct createInventoryProduct(Product product, Long stock);
}
