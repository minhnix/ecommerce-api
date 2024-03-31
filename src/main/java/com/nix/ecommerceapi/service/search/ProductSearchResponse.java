package com.nix.ecommerceapi.service.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchResponse {
    private Long productId;
    private String productName;
    private String productDescription;
    private Long productPrice;
    private Long productPriceAfterDiscount;
    private String productSlug;
    private String productImage;
    private Long inventoryStock;
    private Long totalSold;
    private Long categoryId;
}
