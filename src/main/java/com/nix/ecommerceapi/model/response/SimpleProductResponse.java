package com.nix.ecommerceapi.model.response;

import com.nix.ecommerceapi.model.entity.Category;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleProductResponse {
    private Long id;
    private String name;
    private String slug;
    private Category category;
    private String description;
    private String image;
    private Long price;
    private Long priceAfterDiscount;
    private boolean isVariant;
}
