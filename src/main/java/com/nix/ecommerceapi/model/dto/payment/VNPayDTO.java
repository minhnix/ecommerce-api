package com.nix.ecommerceapi.model.dto.payment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VNPayDTO extends PaymentDTO {
    private String bankCode;
    private String language;
    private String ipAddress;
    private String orderInfo;
}
