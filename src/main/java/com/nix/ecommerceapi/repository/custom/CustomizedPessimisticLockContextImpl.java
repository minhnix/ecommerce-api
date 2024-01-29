package com.nix.ecommerceapi.repository.custom;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class CustomizedPessimisticLockContextImpl extends CustomizedPessimisticLockContext {
    public CustomizedPessimisticLockContextImpl(EntityManager em) {
        super(em);
    }

}
