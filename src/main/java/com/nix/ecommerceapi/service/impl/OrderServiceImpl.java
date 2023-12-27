package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.enumuration.OrderStatus;
import com.nix.ecommerceapi.model.response.CheckoutResponse;
import com.nix.ecommerceapi.repository.OrderRepository;
import com.nix.ecommerceapi.repository.UserRepository;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.OrderDetailService;
import com.nix.ecommerceapi.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    @Transactional
    @Override
    public Order createOrder(CheckoutResponse checkoutResponse, CustomUserDetails user, String address, String payment) {
        Order order = new Order();
        order.setAddress(address);
        order.setNote("");
        order.setStatus(OrderStatus.PENDING);
        order.setUser(userRepository.getReferenceById(user.getId()));
        order.setTotalDiscount(checkoutResponse.getTotalDiscount());
        order.setTotalPrice(checkoutResponse.getTotalPrice());
        order.setTotalShippingCost(checkoutResponse.getTotalShippingCost());
        order.setTotalCheckoutPrice(checkoutResponse.getTotalCheckoutPrice());
        order.setTrackingNumber("#01010101");
        Order savedOrder = orderRepository.save(order);
        orderDetailService.batchInsertOrderDetail(savedOrder, checkoutResponse.getProducts());
        return savedOrder;
    }
}
