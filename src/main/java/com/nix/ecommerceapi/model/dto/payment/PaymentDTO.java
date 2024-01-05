package com.nix.ecommerceapi.model.dto.payment;

import com.nix.ecommerceapi.model.entity.Payment;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private Payment.PaymentType paymentType;
}
