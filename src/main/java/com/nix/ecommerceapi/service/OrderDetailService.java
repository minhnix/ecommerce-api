package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.response.CartResponse;

import java.util.List;

public interface OrderDetailService {
    void batchInsertOrderDetail(Order order, List<CartResponse> cartResponses);
}
