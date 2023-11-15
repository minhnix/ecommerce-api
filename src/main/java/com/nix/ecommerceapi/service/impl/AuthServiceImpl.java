package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.AuthFailureException;
import com.nix.ecommerceapi.model.entity.KeyStore;
import com.nix.ecommerceapi.model.entity.User;
import com.nix.ecommerceapi.model.enumuration.RoleName;
import com.nix.ecommerceapi.model.request.LoginRequest;
import com.nix.ecommerceapi.model.request.SignUpRequest;
import com.nix.ecommerceapi.repository.KeyStoreRepository;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.security.jwt.JwtPayload;
import com.nix.ecommerceapi.security.jwt.JwtUtils;
import com.nix.ecommerceapi.security.jwt.Token;
import com.nix.ecommerceapi.service.AuthService;
import com.nix.ecommerceapi.service.KeyStoreService;
import com.nix.ecommerceapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final KeyStoreService keyStoreService;
    private final KeyStoreRepository keyStoreRepository;

    @Override
    public User registerCustomer(SignUpRequest user) {
        User user1 = new User();
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        User savedUser = userService.createUser(user1, RoleName.ROLE_CUSTOMER);
        keyStoreService.createNewKeyStore(
                savedUser.getId(),
                null
        );
        return savedUser;
    }

    @Override
    @Transactional
    public Token login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        JwtPayload payload = new JwtPayload(user.getId(), user.getUsername(), user.getRole());
        Token token = jwtUtils.generateTokenPair(payload);
        keyStoreService.updateRefreshToken(
                ((CustomUserDetails) authentication.getPrincipal()).getId(),
                token.getRefreshToken(),
                null
        );
        return token;
    }

    @Override
    @Transactional
    public Token doRefreshToken(String refreshToken) {
        JwtPayload payload = jwtUtils.getPayLoadIfTokenValidated(refreshToken);
        Long userId = payload.getUserId();
        KeyStore keyStore = keyStoreRepository.findByUserId(userId)
                .orElseThrow(() -> new AuthFailureException("Invalid user"));
        if (Objects.equals(keyStore.getRefreshToken(), refreshToken)) {
            Token token = jwtUtils.generateTokenPair(payload);
            keyStore.setRefreshToken(token.getRefreshToken());
            keyStoreRepository.save(keyStore);
            return token;
        } else throw new AuthFailureException("Invalid token");
    }
}