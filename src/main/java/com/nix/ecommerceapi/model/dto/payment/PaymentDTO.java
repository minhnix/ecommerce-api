package com.nix.ecommerceapi.model.dto.payment;

import com.nix.ecommerceapi.service.payment.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
    @NotNull
    private Long amount;
    @NotNull
    private PaymentType paymentType;
}
