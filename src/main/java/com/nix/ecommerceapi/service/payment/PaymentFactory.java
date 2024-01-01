package com.nix.ecommerceapi.service.payment;

import com.nix.ecommerceapi.model.entity.Payment;

public interface PaymentFactory {
    PaymentService create(Payment.PaymentType paymentType);
}
