package com.nix.ecommerceapi.model.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    @JsonProperty("Message")
    private String message;
    @JsonProperty("RspCode")
    private String code;

}
