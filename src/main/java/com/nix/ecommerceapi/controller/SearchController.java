package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.model.constants.PageConstants;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.service.search.ProductSearchRequest;
import com.nix.ecommerceapi.service.search.ProductSearchResponse;
import com.nix.ecommerceapi.service.search.ProductSearchService;
import com.nix.ecommerceapi.utils.PageableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    private final ProductSearchService productSearchService;

    public SearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @GetMapping
    public PagedResponse<ProductSearchResponse> findAllUnPublishProduct(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page", defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = PageConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDir", required = false) String sortDir,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "maxPrice", required = false) Long maxPrice,
            @RequestParam(value = "minPrice", required = false) Long minPrice,
            @RequestParam(value = "rating", required = false) Integer rating
    ) {
        Pageable pageable = PageableUtils.getPageable(page, size, sortBy, sortDir);
        ProductSearchRequest productSearchRequest = ProductSearchRequest.builder()
                .keyword(keyword)
                .categoryId(categoryId)
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .rating(rating)
                .build();
        return productSearchService.searchProduct(productSearchRequest,pageable);
    }

}
