package com.nix.ecommerceapi.service.payment.impl;

import com.nix.ecommerceapi.config.VNPayProperties;
import com.nix.ecommerceapi.model.dto.payment.PaymentDTO;
import com.nix.ecommerceapi.model.dto.payment.VNPayDTO;
import com.nix.ecommerceapi.service.payment.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class VNPayPaymentService implements PaymentService {
    private final VNPayProperties vnpayProperties;

    @Override
    public Object pay(PaymentDTO paymentDTO) {
        VNPayDTO payDTO = (VNPayDTO) paymentDTO;
        return pay(payDTO.getAmount(), payDTO.getBankCode(), payDTO.getLanguage(), payDTO.getIpAddress());
    }

    private String pay(Long amount, String bankCode, String language, String ipAddress) {
        Map<String, String> vnp_Params = setAndGetVnpParams(amount, bankCode, language, ipAddress);
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        String queryUrl = buildQueryUrl(vnp_Params, fieldNames);
        String hashData = buildHashData(vnp_Params, fieldNames);
        String vnp_SecureHash = hmacSHA512(vnpayProperties.getHashSecret(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return vnpayProperties.getPayUrl() + "?" + queryUrl; // payment url
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

    private Map<String, String> setAndGetVnpParams(Long amount, String bankCode, String language, String ipAddress) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnpayProperties.getVersion());
        vnp_Params.put("vnp_Command", vnpayProperties.getCommand());
        vnp_Params.put("vnp_TmnCode", vnpayProperties.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", vnpayProperties.getCurrCode());
        if (bankCode != null && bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", getRandomNumber(8));
        vnp_Params.put("vnp_OrderInfo", vnpayProperties.getOrderInfo());
        vnp_Params.put("vnp_OrderType", vnpayProperties.getOrderType());
        if (language != null && !language.isEmpty()) {
            vnp_Params.put("vnp_Locale", language);
        } else {
            vnp_Params.put("vnp_Locale", vnpayProperties.getLocale());
        }
        vnp_Params.put("vnp_ReturnUrl", vnpayProperties.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", ipAddress);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnp_Params;
    }

    private String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

}
