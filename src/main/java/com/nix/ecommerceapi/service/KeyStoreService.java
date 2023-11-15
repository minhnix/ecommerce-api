package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.KeyStore;

public interface KeyStoreService {
    KeyStore createNewKeyStore(Long userId, String refreshToken);
    KeyStore updateRefreshToken(Long userId, String newRefreshToken, String oldRefreshToken);
}