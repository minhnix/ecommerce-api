package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.annotation.CurrentUser;
import com.nix.ecommerceapi.exception.AuthFailureException;
import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.request.CheckoutRequest;
import com.nix.ecommerceapi.model.response.ApiResponse;
import com.nix.ecommerceapi.model.response.CheckoutResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping("/review")
    public CheckoutResponse checkoutReview(@RequestBody CheckoutRequest checkoutRequest, @CurrentUser CustomUserDetails user) {
        if (user == null) throw new AuthFailureException("Full authentication to get resource");
        return checkoutService.checkoutReview(checkoutRequest, user);
    }

    @PostMapping("/order")
    public ApiResponse order(@RequestBody CheckoutRequest checkoutRequest, @CurrentUser CustomUserDetails user) {
        if (user == null) throw new AuthFailureException("Full authentication to get resource");
        Order order = checkoutService.order(checkoutRequest, user);
        return ApiResponse.success(order, "Order successfully");
    }
}
