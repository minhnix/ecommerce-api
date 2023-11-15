package com.nix.ecommerceapi.repository;

import com.nix.ecommerceapi.model.entity.KeyStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeyStoreRepository extends JpaRepository<KeyStore, Long> {
    Optional<KeyStore> findByUserId(Long userId);
}
