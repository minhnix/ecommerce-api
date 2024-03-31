package com.nix.ecommerceapi.service.search;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductSearchRequest {
    public String keyword;
    public Long categoryId;
    public Long maxPrice;
    public Long minPrice;
    public Integer rating;
}
