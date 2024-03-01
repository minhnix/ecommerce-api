package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.entity.Inventory;
import com.nix.ecommerceapi.model.entity.Model;
import com.nix.ecommerceapi.repository.InventoryRepository;
import com.nix.ecommerceapi.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public Inventory createInventory(Model model, Long stock) {
        Inventory inventory = new Inventory();
        inventory.setModel(model);
        inventory.setStock(stock);
        inventory.setTotalSold(0L);
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
        inventory.setTotalSold(inventory.getTotalSold() + quantity);
        return inventoryRepository.save(inventory);
    }
}
