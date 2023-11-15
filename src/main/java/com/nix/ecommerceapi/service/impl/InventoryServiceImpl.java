package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.model.entity.Inventory;
import com.nix.ecommerceapi.model.entity.Model;
import com.nix.ecommerceapi.repository.InventoryRepository;
import com.nix.ecommerceapi.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    @Override
    public Inventory createInventory(Model model, Long stock) {
        Inventory inventory = new Inventory();
        inventory.setModel(model);
        inventory.setStock(stock);
        return inventoryRepository.save(inventory);
    }
}
