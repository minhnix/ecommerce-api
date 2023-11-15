package com.nix.ecommerceapi.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    @NotNull
    private Long modelId;
    @NotNull
    private Long quantity;
}
