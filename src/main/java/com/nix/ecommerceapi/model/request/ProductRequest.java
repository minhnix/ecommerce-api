package com.nix.ecommerceapi.model.request;

import com.nix.ecommerceapi.model.dto.AttributeDTO;
import com.nix.ecommerceapi.model.dto.ProductOptionsDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequest {
    @NotNull
    private Long categoryId;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @Valid
    private List<AttributeDTO> attributes;
    private Long priceAfterDiscount;
    @NotNull
    private Long price;
    private Long quantity;
    private String image;
    @Valid
    private List<ProductOptionsDTO> options;
    @Valid
    private List<VariantRequest> variants;
    private boolean isPublished = false;
    private boolean isVariant;
}
