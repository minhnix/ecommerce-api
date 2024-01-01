package com.nix.ecommerceapi.service.payment;

import com.nix.ecommerceapi.model.dto.payment.PaymentDTO;
import com.nix.ecommerceapi.model.entity.Payment;
import com.nix.ecommerceapi.security.CustomUserDetails;

public interface PaymentService {
    Object pay(PaymentDTO paymentDTO, CustomUserDetails user);
    Payment save(Payment payment);
}
