package com.nix.ecommerceapi.repository.custom;

import com.nix.ecommerceapi.model.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomizedProductRepositoryImpl implements CustomizedProductRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Product> findAllByPublished(Pageable pageable, boolean isPublished) {
        List<Long> productIds = em.createQuery("""
                            select p.id from Product p where p.isPublished = :isPublished
                        """, Long.class)
                .setParameter("isPublished", isPublished)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return null;
    }
}
