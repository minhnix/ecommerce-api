package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.dto.OrderInfo;
import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.response.CheckoutResponse;

public interface OrderService {
    Order createOrder(CheckoutResponse checkoutResponse, OrderInfo orderInfo);
}
