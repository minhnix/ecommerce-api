package com.nix.ecommerceapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.vnpay")
@Getter
@Setter
public class VNPayProperties {
    private String version = "2.1.0";
    private String command = "pay";
    private String locale = "vn";
    private String currCode = "VND";
    private String orderInfo = "Payment for order";
    private String orderType = "250000"; // Danh mục hàng hoá VNPAY thời trang
    private String tmnCode;
    private String hashSecret;
    private String returnUrl = "http://localhost:8080/api/v1/payment/pay-result"; // redirect to url after payment
    private String payUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
}
