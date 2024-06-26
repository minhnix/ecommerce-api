package com.nix.ecommerceapi.service.payment.impl;

import com.nix.ecommerceapi.config.VNPayProperties;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.dto.payment.PaymentDTO;
import com.nix.ecommerceapi.model.dto.payment.PaymentResponse;
import com.nix.ecommerceapi.model.dto.payment.VNPayDTO;
import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.entity.OrderEntityGraph;
import com.nix.ecommerceapi.model.entity.Payment;
import com.nix.ecommerceapi.model.entity.PaymentEntityGraph;
import com.nix.ecommerceapi.model.enumuration.OrderStatus;
import com.nix.ecommerceapi.repository.OrderRepository;
import com.nix.ecommerceapi.repository.PaymentRepository;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.payment.PaymentService;
import com.nix.ecommerceapi.service.payment.PaymentValidator;
import com.nix.ecommerceapi.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class VNPayPaymentService implements PaymentService {
    private final VNPayProperties vnpayProperties;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Object pay(PaymentDTO paymentDTO, CustomUserDetails user) {
        VNPayDTO payDTO = (VNPayDTO) paymentDTO;
        Order order = orderRepository.findById(payDTO.getOrderId(), OrderEntityGraph.____().payment().____.____())
                .orElseThrow(() -> new NotFoundException("Order not found"));
        PaymentValidator.validate(order, payDTO, user.getId());
        return this.pay(order.getPayment(), payDTO);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Object handleIpnUrl(HttpServletRequest request) {
        PaymentResponse paymentResponse = new PaymentResponse();
        try {
            Map<String, String> fields = getParamFromRequest(request);
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            String signValue = SecurityUtils.hashAllFields(fields, vnpayProperties.getHashSecret());
            if (signValue.equals(vnp_SecureHash)) {
                Payment payment = paymentRepository.findByPaymentId(
                                request.getParameter("vnp_TxnRef"),
                                PaymentEntityGraph.____().order().____.____())
                        .orElseThrow(() -> new NotFoundException("Payment not found"));
                boolean checkOrderId = payment.getOrder().getId() != null;
                double amount = payment.getAmount() * 100;
                long vnp_Amount = Long.parseLong(request.getParameter("vnp_Amount"));
                boolean checkAmount = (long) amount == vnp_Amount;
                boolean checkOrderStatus = true;
                if (payment.getPaymentMethod() != Payment.PaymentMethod.COD) {
                    checkOrderStatus = payment.getOrder().getStatus() == OrderStatus.PENDING;
                }

                if (checkOrderId) {
                    if (checkAmount) {
                        if (checkOrderStatus) {
                            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                                payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);
                                payment.getOrder().setStatus(OrderStatus.PROCESSING);
                            } else {
                                payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
                            }
                            paymentResponse.setMessage("Confirm Success");
                            paymentResponse.setCode("00");
                            payment.setTransactionId(request.getParameter("vnp_TransactionNo"));
                            paymentRepository.save(payment);
                        } else {
                            paymentResponse.setCode("02");
                            paymentResponse.setMessage("Order already confirmed");
                        }
                    } else {
                        paymentResponse.setCode("04");
                        paymentResponse.setMessage("Invalid Amount");
                    }
                } else {
                    paymentResponse.setCode("01");
                    paymentResponse.setMessage("Order not found");
                }
            } else {
                paymentResponse.setCode("97");
                paymentResponse.setMessage("Invalid Checksum");
            }
        } catch (Exception e) {
            paymentResponse.setCode("99");
            paymentResponse.setMessage("Unknow error");
        }
        return paymentResponse;
    }

    public Object returnUrl(HttpServletRequest request) {
        Map<String, String> vnpParams = getParamFromRequest(request);
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        String signValue = SecurityUtils.hashAllFields(vnpParams, vnpayProperties.getHashSecret());
        PaymentResponse paymentResponse = new PaymentResponse();
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                paymentResponse.setCode("00");
                paymentResponse.setMessage("Transaction Success");
            } else {
                paymentResponse.setCode(request.getParameter("vnp_ResponseCode"));
                paymentResponse.setMessage("Transaction Failed");
            }
        } else {
            paymentResponse.setCode(request.getParameter("vnp_ResponseCode"));
            paymentResponse.setMessage("Invalid signature");
        }
        return paymentResponse;
    }

    private String pay(Payment payment, VNPayDTO vnPayDTO) {
        Map<String, String> vnp_Params = setAndGetVnpParams(vnPayDTO);
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        String queryUrl = buildQueryUrl(vnp_Params, fieldNames);
        String hashData = buildHashData(vnp_Params, fieldNames);
        String vnp_SecureHash = SecurityUtils.hmacSHA512(vnpayProperties.getHashSecret(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        this.updatePayment(payment, vnp_Params);
        return vnpayProperties.getPayUrl() + "?" + queryUrl; // payment url
    }

    private Payment updatePayment(Payment payment, Map<String, String> vnp_Params) {
        payment.setPaymentId(vnp_Params.get("vnp_TxnRef"));
        payment.setTransactionDate(vnp_Params.get("vnp_CreateDate"));
        return this.save(payment);
    }

    private Map<String, String> getParamFromRequest(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName, fieldValue;
            fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        return fields;
    }

    private String buildHashData(Map<String, String> vnp_Params, List<String> fieldNames) {
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }
        return hashData.toString();
    }

    private String buildQueryUrl(Map<String, String> vnp_Params, List<String> fieldNames) {
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                }
            }
        }
        return query.toString();
    }

    private Map<String, String> setAndGetVnpParams(VNPayDTO vnPayDTO) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnpayProperties.getVersion());
        vnp_Params.put("vnp_Command", vnpayProperties.getCommand());
        vnp_Params.put("vnp_TmnCode", vnpayProperties.getTmnCode());
        double amount = vnPayDTO.getAmount() * 100;
        vnp_Params.put("vnp_Amount", String.valueOf((long) amount));
        vnp_Params.put("vnp_CurrCode", vnpayProperties.getCurrCode());
        if (vnPayDTO.getBankCode() != null && vnPayDTO.getBankCode().isEmpty()) {
            vnp_Params.put("vnp_BankCode", vnPayDTO.getBankCode());
        }
        String vnp_TxnRef = vnPayDTO.getOrderId().toString() + getRandom(8);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnpayProperties.getOrderInfo());
        vnp_Params.put("vnp_OrderType", vnpayProperties.getOrderType());
        if (vnPayDTO.getLanguage() != null && !vnPayDTO.getLanguage().isEmpty()) {
            vnp_Params.put("vnp_Locale", vnPayDTO.getLanguage());
        } else {
            vnp_Params.put("vnp_Locale", vnpayProperties.getLocale());
        }
        vnp_Params.put("vnp_ReturnUrl", vnpayProperties.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnPayDTO.getIpAddress());
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnp_Params;
    }

    private String getRandom(int len) {
        Random rnd = new Random();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
