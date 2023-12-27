package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.response.CheckoutResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;

public interface OrderService {
    Order createOrder(CheckoutResponse checkoutResponse, CustomUserDetails user, String address, String payment);
}
