package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.entity.Inventory;
import com.nix.ecommerceapi.model.entity.Model;
import com.nix.ecommerceapi.repository.InventoryRepository;
import com.nix.ecommerceapi.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public Inventory createInventory(Model model, Long stock) {
        Inventory inventory = new Inventory();
        inventory.setModel(model);
        inventory.setStock(stock);
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory updateInventory(Long id, Long newStock) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Inventory not found"));
        inventory.setStock(newStock);
        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public Inventory processOrderAndSaveInventory(Long modelId, Long quantity) {
        Inventory inventory = inventoryRepository.findById(modelId).orElseThrow(() -> new NotFoundException("Inventory not found"));
        if (inventory.getStock() < quantity) {
            throw new BadRequestException("Stock is not enough");
        }
        inventory.setStock(inventory.getStock() - quantity);
        return inventoryRepository.save(inventory);
    }
}
