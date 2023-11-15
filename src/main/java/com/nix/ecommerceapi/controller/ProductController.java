package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.annotation.CurrentUser;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.request.ProductRequest;
import com.nix.ecommerceapi.model.response.ApiResponse;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.model.response.SimpleProductResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.ProductService;
import com.nix.ecommerceapi.model.constants.PageConstants;
import com.nix.ecommerceapi.utils.PageableUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SHOP')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.createProduct(productRequest);
        return new ApiResponse(product, "Create product", HttpStatus.CREATED.value());
    }

    @PutMapping("/publish/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHOP')")
    public ApiResponse publishProduct(@PathVariable("id") Long id) {
        SimpleProductResponse product = productService.publishProduct(id);
        return new ApiResponse(product, "product published", HttpStatus.OK.value());
    }

    @PutMapping("/un-publish/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHOP')")
    public ApiResponse unPublishProduct(@PathVariable("id") Long id) {
        SimpleProductResponse product = productService.unPublishProduct(id);
        return new ApiResponse(product, "Product unpublished", HttpStatus.OK.value());
    }

    @GetMapping("/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHOP')")
    public ApiResponse findAllPublishProduct(
            @RequestParam(value = "page", defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = PageConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDir", required = false) String sortDir

    ) {
        Pageable pageable = PageableUtils.getPageable(page, size, sortBy, sortDir);
        PagedResponse<SimpleProductResponse> products = productService.findAllPublishProduct(pageable);
        return new ApiResponse(products, "Product published", HttpStatus.OK.value());
    }

    @GetMapping("/un-publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHOP')")
    public ApiResponse findAllUnPublishProduct(
            @RequestParam(value = "page", defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = PageConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDir", required = false) String sortDir

    ) {
        Pageable pageable = PageableUtils.getPageable(page, size, sortBy, sortDir);
        PagedResponse<SimpleProductResponse> products = productService.findAllUnPublishProduct(pageable);
        return new ApiResponse(products, "Product unpublished", HttpStatus.OK.value());
    }

    @GetMapping("/{id}")
    public ApiResponse findOneProduct(@PathVariable("id") Long id, @CurrentUser CustomUserDetails user) {
        return new ApiResponse(productService.findOneProduct(id, user), "Find one product", HttpStatus.OK.value());
    }

}
