package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.User;
import com.nix.ecommerceapi.model.request.LoginRequest;
import com.nix.ecommerceapi.model.request.SignUpRequest;
import com.nix.ecommerceapi.security.jwt.Token;

public interface AuthService {
    User registerCustomer(SignUpRequest user);
    Token login(LoginRequest loginRequest);
    Token doRefreshToken(String refreshToken);
}

