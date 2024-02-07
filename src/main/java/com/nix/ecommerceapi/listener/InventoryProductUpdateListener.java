package com.nix.ecommerceapi.listener;

import com.nix.ecommerceapi.event.InventoryProductUpdateEvent;
import com.nix.ecommerceapi.event.InventoryProductUpdateStockEvent;
import com.nix.ecommerceapi.event.InventoryProductUpdateTotalSoldEvent;
import com.nix.ecommerceapi.model.entity.InventoryProduct;
import com.nix.ecommerceapi.repository.InventoryProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
@Transactional
public class InventoryProductUpdateListener {
    private final InventoryProductRepository repository;

    @Async
    @EventListener
    public void updateListener(InventoryProductUpdateEvent event) {
        InventoryProduct inventoryProduct = repository.getByIdAndObtainPessimisticWriteLocking(event.id());
        inventoryProduct.setStock(inventoryProduct.getStock() - event.amount());
        inventoryProduct.setTotalSold(inventoryProduct.getTotalSold() + event.amount());
        repository.save(inventoryProduct);
    }

    @Async
    @EventListener
    public void updateListener(InventoryProductUpdateStockEvent event) {
        InventoryProduct inventoryProduct = repository.getByIdAndObtainPessimisticWriteLocking(event.id());
        inventoryProduct.setStock(inventoryProduct.getStock() + event.amount());
        repository.save(inventoryProduct);
    }

    @Async
    @EventListener
    public void updateListener(InventoryProductUpdateTotalSoldEvent event) {
        InventoryProduct inventoryProduct = repository.getByIdAndObtainPessimisticWriteLocking(event.id());
        inventoryProduct.setTotalSold(inventoryProduct.getTotalSold() + event.amount());
        repository.save(inventoryProduct);
    }
}
