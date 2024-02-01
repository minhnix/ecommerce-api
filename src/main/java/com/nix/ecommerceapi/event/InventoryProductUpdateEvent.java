package com.nix.ecommerceapi.event;

public record InventoryProductUpdateEvent(Long id, Long stock, Long totalSold) {
}