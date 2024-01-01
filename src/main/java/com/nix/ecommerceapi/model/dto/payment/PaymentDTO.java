package com.nix.ecommerceapi.model.dto.payment;

import com.nix.ecommerceapi.model.entity.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {
    @NotNull
    private Long orderId;
    @NotNull
    private Double amount;
    @NotNull
    private Payment.PaymentType paymentType;
}
