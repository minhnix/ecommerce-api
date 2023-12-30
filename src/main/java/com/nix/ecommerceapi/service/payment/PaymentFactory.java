package com.nix.ecommerceapi.service.payment;

public interface PaymentFactory {
    PaymentService create(PaymentType paymentType);
}
