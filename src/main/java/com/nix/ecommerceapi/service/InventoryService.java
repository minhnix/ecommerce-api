package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Inventory;
import com.nix.ecommerceapi.model.entity.Model;

public interface InventoryService {
    Inventory createInventory(Model model, Long stock);
}
