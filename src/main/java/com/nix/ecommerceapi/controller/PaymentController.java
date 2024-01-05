package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.annotation.CurrentUser;
import com.nix.ecommerceapi.model.dto.payment.PaymentDTO;
import com.nix.ecommerceapi.model.dto.payment.PaymentResponse;
import com.nix.ecommerceapi.model.dto.payment.VNPayDTO;
import com.nix.ecommerceapi.model.entity.Payment;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.payment.PaymentFactory;
import com.nix.ecommerceapi.service.payment.impl.VNPayPaymentService;
import com.nix.ecommerceapi.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentFactory paymentFactory;

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @Valid @RequestBody PaymentDTO paymentDTO, @CurrentUser CustomUserDetails user) {
        if (paymentDTO.getPaymentType() == Payment.PaymentType.VNPAY) {
            VNPayDTO vnPayDTO = new VNPayDTO();
            vnPayDTO.setAmount(paymentDTO.getAmount());
            vnPayDTO.setIpAddress(WebUtils.getIpAddress(request));
            vnPayDTO.setOrderId(paymentDTO.getOrderId());
            String result = (String) paymentFactory.create(paymentDTO.getPaymentType()).pay(vnPayDTO, user);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vnpay/result")
    public ResponseEntity<?> payResult(HttpServletRequest request) {
        VNPayPaymentService vnPayPaymentService = (VNPayPaymentService) paymentFactory.create(Payment.PaymentType.VNPAY);
        PaymentResponse paymentResponse = (PaymentResponse) vnPayPaymentService.returnUrl(request);
        return ResponseEntity.ok(paymentResponse);
    }

    @GetMapping("/ipn/vnpay")
    public ResponseEntity<?> ipnUrl(HttpServletRequest request) {
        VNPayPaymentService vnPayPaymentService = (VNPayPaymentService) paymentFactory.create(Payment.PaymentType.VNPAY);
        Object result = vnPayPaymentService.handleIpnUrl(request);
        return ResponseEntity.ok(result);
    }
}
