package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.annotation.CurrentUser;
import com.nix.ecommerceapi.exception.AuthFailureException;
import com.nix.ecommerceapi.mapper.CartMapper;
import com.nix.ecommerceapi.model.dto.CartDTO;
import com.nix.ecommerceapi.model.entity.Cart;
import com.nix.ecommerceapi.model.request.CartRequest;
import com.nix.ecommerceapi.model.response.CartResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public List<CartResponse> getCartByCurrentUser(@CurrentUser CustomUserDetails user) {
        if (user == null) throw new AuthFailureException("Full authentication to get resource");
        return cartService.getAllCartByUser(user);
    }

    @GetMapping("/amount")
    public Integer getAmountCart(@CurrentUser CustomUserDetails user) {
        if (user == null) throw new AuthFailureException("Full authentication to get resource");
        return cartService.getAmountCart(user);
    }

    @PostMapping
    public CartDTO createOrUpdateCartItem(@CurrentUser CustomUserDetails user, @RequestBody CartRequest cartRequest) {
        if (user == null) throw new AuthFailureException("Full authentication to get resource");
        return cartService.addProductToCart(cartRequest, user);
    }

    @PutMapping("/{id}")
    public CartDTO updateCartQuantity(@PathVariable("id") Long id, @RequestBody CartRequest cartRequest, @CurrentUser CustomUserDetails user) {
        if (user == null) throw new AuthFailureException("Full authentication to get resource");
        return cartService.updateCartQuantity(id, cartRequest, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(@PathVariable("id") Long id, @CurrentUser CustomUserDetails user) {
        cartService.deleteCart(id, user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllCart(@CurrentUser CustomUserDetails user) {
        cartService.deleteAllCart(user);
    }
}
