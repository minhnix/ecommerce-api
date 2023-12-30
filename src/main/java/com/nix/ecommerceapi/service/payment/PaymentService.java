package com.nix.ecommerceapi.service.payment;

import com.nix.ecommerceapi.model.dto.payment.PaymentDTO;

public interface PaymentService {
    Object pay(PaymentDTO paymentDTO);
}
