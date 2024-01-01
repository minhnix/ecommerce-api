package com.nix.ecommerceapi.service.payment.impl;

import com.nix.ecommerceapi.model.dto.payment.PaymentDTO;
import com.nix.ecommerceapi.model.entity.Payment;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.payment.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class ZaloPaymentService implements PaymentService {
    @Override
    public Object pay(PaymentDTO paymentDTO, CustomUserDetails user) {
        return null;
    }

    @Override
    public Payment save(Payment payment) {
        return null;
    }
}
