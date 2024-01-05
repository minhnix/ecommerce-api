package com.nix.ecommerceapi.service.payment;

import com.nix.ecommerceapi.exception.AuthFailureException;
import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.model.dto.payment.PaymentDTO;
import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.entity.Payment;
import com.nix.ecommerceapi.model.enumuration.OrderStatus;

public class PaymentValidator {
    private PaymentValidator() {
    }

    public static void validate(Order order, PaymentDTO payDTO, Long userId) {
        if (!order.getUser().getId().equals(userId)) {
            throw new AuthFailureException("Full authentication is required to access this resource");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Failed");
        }
        if (!order.getTotalCheckoutPrice().equals(payDTO.getAmount())) {
            throw new BadRequestException("Failed");
        }
        if (order.getPayment().getPaymentMethod() == Payment.PaymentMethod.COD) {
            throw new BadRequestException("Your payment method is COD");
        }
        if (order.getPayment().getPaymentStatus() == Payment.PaymentStatus.SUCCESS) {
            throw new BadRequestException("Your order is paid");
        }
    }
}
