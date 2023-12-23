package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Cart;
import com.nix.ecommerceapi.model.request.CartRequest;
import com.nix.ecommerceapi.model.response.CartResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;

import java.util.List;

public interface CartService {
    Cart addProductToCart(CartRequest cartRequest, CustomUserDetails user);
    Cart updateCartQuantity(Long cartId, CartRequest cartRequest, CustomUserDetails user);
    void deleteCart(Long cartId, CustomUserDetails user);
    void deleteAllCart(CustomUserDetails user);
    int getAmountCart(CustomUserDetails user);
    List<CartResponse> getAllCartByUser(CustomUserDetails user);
}
