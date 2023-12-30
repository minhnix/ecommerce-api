package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.model.dto.payment.PaymentDTO;
import com.nix.ecommerceapi.model.dto.payment.VNPayDTO;
import com.nix.ecommerceapi.service.payment.PaymentFactory;
import com.nix.ecommerceapi.service.payment.PaymentType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentFactory paymentFactory;

    @PostMapping("/pay")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestBody PaymentDTO paymentDTO) {
        if (paymentDTO.getPaymentType() == PaymentType.VNPAY) {
            VNPayDTO vnPayDTO = new VNPayDTO();
            vnPayDTO.setAmount(paymentDTO.getAmount());
            vnPayDTO.setIpAddress(getIpAddress(request));
            String result = (String) paymentFactory.create(paymentDTO.getPaymentType()).pay(vnPayDTO);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok().build();
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
