package com.nix.ecommerceapi.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartQuantity {
    @NotNull
    private Long quantity;
    @NotNull
    private Long cartId;
}
