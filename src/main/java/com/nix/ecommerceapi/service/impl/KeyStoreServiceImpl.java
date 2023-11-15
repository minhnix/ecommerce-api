package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.entity.KeyStore;
import com.nix.ecommerceapi.model.entity.User;
import com.nix.ecommerceapi.repository.KeyStoreRepository;
import com.nix.ecommerceapi.repository.UserRepository;
import com.nix.ecommerceapi.service.KeyStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyStoreServiceImpl implements KeyStoreService {
    private final KeyStoreRepository keyStoreRepository;
    private final UserRepository userRepository;
    @Override
    public KeyStore createNewKeyStore(Long userId, String refreshToken) {
        KeyStore keyStore = new KeyStore();
        User user = userRepository.getReferenceById(userId);
        keyStore.setUser(user);
        keyStore.setRefreshToken(refreshToken);
        return keyStoreRepository.save(keyStore);
    }

    @Override
    public KeyStore updateRefreshToken(Long userId, String newRefreshToken, String oldRefreshToken) {
        KeyStore keyStore = keyStoreRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Not found key store"));
        keyStore.setRefreshToken(newRefreshToken);
        return keyStoreRepository.save(keyStore);
    }

}
