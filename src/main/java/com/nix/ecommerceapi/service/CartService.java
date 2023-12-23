package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.dto.CartDTO;
import com.nix.ecommerceapi.model.request.CartRequest;
import com.nix.ecommerceapi.model.response.CartResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(CartRequest cartRequest, CustomUserDetails user);
    CartDTO updateCartQuantity(Long cartId, CartRequest cartRequest, CustomUserDetails user);
    void deleteCart(Long cartId, CustomUserDetails user);
    void deleteAllCart(CustomUserDetails user);
    int getAmountCart(CustomUserDetails user);
    List<CartResponse> getAllCartByUser(CustomUserDetails user);
}
