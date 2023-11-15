package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Discount;
import com.nix.ecommerceapi.model.request.DiscountRequest;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;

public interface DiscountService {
    Discount createDiscount(DiscountRequest discountRequest);
    PagedResponse<Discount> getAllDiscount(Pageable pageable, CustomUserDetails user, long totalOrderValue);
    Discount activateDiscount(Long id);
    Discount updateDiscount(Long id, DiscountRequest discountRequest);
    void deleteDiscount(Long id);
    void applyDiscount(String code, CustomUserDetails user); // todo: after impl order, cart
    void cancelDiscount(String code, CustomUserDetails user);
}
