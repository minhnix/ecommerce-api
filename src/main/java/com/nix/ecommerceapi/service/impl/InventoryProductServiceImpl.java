package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.entity.InventoryProduct;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.repository.InventoryProductRepository;
import com.nix.ecommerceapi.service.InventoryProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryProductServiceImpl implements InventoryProductService {
    private final InventoryProductRepository inventoryProductRepository;

    @Override
    public InventoryProduct createInventoryProduct(Product product, Long stock) {
        InventoryProduct inventoryProduct = new InventoryProduct();
        long stock1 = 0;
        if (stock != null && stock > 0) {
            stock1 = stock;
        }
        inventoryProduct.setStock(stock1);
        inventoryProduct.setLocation("unknown");
        inventoryProduct.setProduct(product);
        inventoryProduct.setTotalSold(0L);
        return inventoryProductRepository.save(inventoryProduct);
    }

    @Override
    public InventoryProduct updateInventoryProduct(Long productId, Long stock, Long totalSold, String location) {
        InventoryProduct inventoryProduct = inventoryProductRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product inventory not found"));
        if (stock != null) inventoryProduct.setStock(stock);
        if (totalSold != null) inventoryProduct.setTotalSold(totalSold);
        if (location != null) inventoryProduct.setLocation(location);
        return inventoryProductRepository.save(inventoryProduct);
    }
}
