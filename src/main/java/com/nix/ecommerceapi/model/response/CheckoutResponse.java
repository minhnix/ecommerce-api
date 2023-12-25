package com.nix.ecommerceapi.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutResponse {
    private List<CartResponse> products;
    private Long discountIdUsed;
    private Double totalPrice;
    private Double totalDiscount;
    private Double totalShippingCost;
    private Double totalCheckoutPrice;

    public void calculateTotalCheckoutPrice() {
        this.totalCheckoutPrice = this.totalPrice - this.totalDiscount + this.totalShippingCost;
    }

    public void calculateTotalPrice() {
        if (products.isEmpty()) return;
        this.totalPrice = this.products.stream().mapToDouble(CartResponse::getTotalCost).sum();
    }
}
