package com.nix.ecommerceapi.service.payment.impl;

import com.nix.ecommerceapi.service.payment.PaymentFactory;
import com.nix.ecommerceapi.service.payment.PaymentService;
import com.nix.ecommerceapi.service.payment.PaymentType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentFactoryImpl implements PaymentFactory {
    private final List<PaymentService> concretePaymentServices;

    public PaymentFactoryImpl(List<PaymentService> concretePaymentServices) {
        this.concretePaymentServices = concretePaymentServices;
    }

    @Override
    public PaymentService create(PaymentType paymentType) {
        return switch (paymentType) {
            case ZALO -> getPaymentService(ZaloPaymentService.class);
            case VNPAY -> getPaymentService(VNPayPaymentService.class);
            case MOMO -> getPaymentService(MomoPaymentService.class);
        };
    }

    private PaymentService getPaymentService(Class<?> type) {
        return (PaymentService) concretePaymentServices.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElse(null);
    }
}
