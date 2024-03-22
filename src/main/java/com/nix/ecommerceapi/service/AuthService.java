package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.request.LoginRequest;
import com.nix.ecommerceapi.model.request.SignUpRequest;
import com.nix.ecommerceapi.model.response.ApiResponse;
import com.nix.ecommerceapi.security.jwt.Token;

public interface AuthService {
    ApiResponse registerCustomer(SignUpRequest user);
    Token login(LoginRequest loginRequest);
    Token doRefreshToken(String refreshToken);
    ApiResponse verifyEmail(String token);
}

