package com.nix.ecommerceapi.event;

import com.nix.ecommerceapi.model.response.CartResponse;

import java.util.List;

public record InventoryProductUpdateEvent(List<CartResponse> products) {
}