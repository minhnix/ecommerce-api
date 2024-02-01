package com.nix.ecommerceapi.repository.custom;

import com.nix.ecommerceapi.model.entity.InventoryProduct;

public interface CustomizedInventoryProductRepository {
    void setLockTimeout(long timeoutDurationInMs);
    long getLockTimeout();
    InventoryProduct getByIdAndObtainPessimisticWriteLocking(Long id);

}
