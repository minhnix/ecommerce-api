package com.nix.ecommerceapi.model.response;

import com.nix.ecommerceapi.model.dto.ExtInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelResponse {
    private Long productId;
    private Long modelId;
    private Long price;
    private Long priceAfterDiscount;
    private Long stock;
    private String name;
    private boolean isPublished;
    private ExtInfo extInfo;
    private String productName;
}
