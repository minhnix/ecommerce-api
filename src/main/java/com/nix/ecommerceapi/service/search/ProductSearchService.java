package com.nix.ecommerceapi.service.search;

import com.nix.ecommerceapi.model.response.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface ProductSearchService {
    PagedResponse<ProductSearchResponse> searchProduct(ProductSearchRequest productSearchRequest, Pageable pageable);
}
