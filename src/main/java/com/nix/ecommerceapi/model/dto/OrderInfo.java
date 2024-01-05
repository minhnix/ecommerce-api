package com.nix.ecommerceapi.model.dto;

import com.nix.ecommerceapi.model.entity.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderInfo {
    private String address;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentType paymentType;
    private Long userId;
}
