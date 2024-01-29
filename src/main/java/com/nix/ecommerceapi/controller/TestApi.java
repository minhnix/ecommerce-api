package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.service.InventoryService;
import com.nix.ecommerceapi.service.locker.RedisLocker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestApi {
    private final InventoryService inventoryService;
    private final RedisLocker locker;

    public TestApi(InventoryService inventoryService, RedisLocker locker) {
        this.inventoryService = inventoryService;
        this.locker = locker;
    }

    @GetMapping("/test")
    public void test() {

        inventoryService.processOrderAndSaveInventory(1L, 1L);
    }
}
