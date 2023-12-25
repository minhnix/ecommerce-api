package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.request.CheckoutRequest;
import com.nix.ecommerceapi.model.response.CheckoutResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;

public interface CheckoutService {
    CheckoutResponse checkoutReview(CheckoutRequest checkoutRequest, CustomUserDetails user);
    void order(CheckoutRequest checkoutRequest, CustomUserDetails user);
}
