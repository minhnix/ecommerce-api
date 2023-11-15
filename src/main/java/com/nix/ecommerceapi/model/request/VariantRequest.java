package com.nix.ecommerceapi.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VariantRequest {
    private boolean isPublished = true;
    private Long price;
    private Long priceAfterDiscount;
    @NotNull
    private String name;
    private Long quantity;
}
