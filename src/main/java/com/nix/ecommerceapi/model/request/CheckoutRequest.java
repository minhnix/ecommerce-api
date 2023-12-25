package com.nix.ecommerceapi.model.request;

import com.nix.ecommerceapi.model.dto.ItemProduct;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutRequest {
    private List<ItemProduct> itemProducts;
    private Long discountId;
}
