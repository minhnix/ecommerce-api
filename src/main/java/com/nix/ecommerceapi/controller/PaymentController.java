package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.model.dto.payment.PaymentDTO;
import com.nix.ecommerceapi.model.dto.payment.PaymentResponse;
import com.nix.ecommerceapi.model.dto.payment.VNPayDTO;
import com.nix.ecommerceapi.service.payment.PaymentFactory;
import com.nix.ecommerceapi.service.payment.PaymentType;
import com.nix.ecommerceapi.service.payment.impl.VNPayPaymentService;
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
    public ResponseEntity<?> createPayment(HttpServletRequest request, @Valid @RequestBody PaymentDTO paymentDTO) {
        if (paymentDTO.getPaymentType() == PaymentType.VNPAY) {
            VNPayDTO vnPayDTO = new VNPayDTO();
            vnPayDTO.setAmount(paymentDTO.getAmount());
            vnPayDTO.setIpAddress(getIpAddress(request));
            vnPayDTO.setOrderId(paymentDTO.getOrderId());
            String result = (String) paymentFactory.create(paymentDTO.getPaymentType()).pay(vnPayDTO);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pay-result")
    public ResponseEntity<?> payResult(HttpServletRequest request) {
        VNPayPaymentService vnPayPaymentService = (VNPayPaymentService) paymentFactory.create(PaymentType.VNPAY);
        PaymentResponse paymentResponse = (PaymentResponse) vnPayPaymentService.returnUrl(request);
        return ResponseEntity.ok(paymentResponse);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

}
