package com.nix.ecommerceapi.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponse {
    private Long id;
    private ModelResponse product;
    private Long quantity;
    private Long totalCost;
    private boolean deleted;
}
