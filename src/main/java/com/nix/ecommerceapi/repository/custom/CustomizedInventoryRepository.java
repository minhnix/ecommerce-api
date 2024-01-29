package com.nix.ecommerceapi.repository.custom;

import com.nix.ecommerceapi.model.entity.Inventory;

public interface CustomizedInventoryRepository {
    void setLockTimeout(long timeoutDurationInMs);
    long getLockTimeout();
    Inventory getByIdAndObtainPessimisticWriteLocking(Long id);
}
