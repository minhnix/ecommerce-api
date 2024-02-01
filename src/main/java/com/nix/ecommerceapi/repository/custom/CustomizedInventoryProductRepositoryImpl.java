package com.nix.ecommerceapi.repository.custom;

import com.nix.ecommerceapi.model.entity.InventoryProduct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomizedInventoryProductRepositoryImpl implements CustomizedInventoryProductRepository{
    private final CustomizedPessimisticLockContext customizedItemRepositoryContext;
    @PersistenceContext
    private final EntityManager em;

    @Override
    public void setLockTimeout(long timeoutDurationInMs) {
        customizedItemRepositoryContext.setLockTimeout(timeoutDurationInMs);
    }

    @Override
    public long getLockTimeout() {
        return customizedItemRepositoryContext.getLockTimeout();
    }

    @Override
    public InventoryProduct getByIdAndObtainPessimisticWriteLocking(Long id) {
        Query query = em.createQuery("select i from InventoryProduct i where i.id = :id");
        query.setParameter("id", id);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        query = customizedItemRepositoryContext.setLockTimeoutIfRequired(query);
        return (InventoryProduct) query.getSingleResult();
    }
}
