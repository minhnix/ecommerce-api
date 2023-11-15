package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.annotation.CurrentUser;
import com.nix.ecommerceapi.model.constants.PageConstants;
import com.nix.ecommerceapi.model.entity.Discount;
import com.nix.ecommerceapi.model.request.DiscountRequest;
import com.nix.ecommerceapi.model.response.ApiResponse;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.DiscountService;
import com.nix.ecommerceapi.utils.PageableUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse createDiscount(@RequestBody @Valid DiscountRequest discountRequest) {
        Discount discount = discountService.createDiscount(discountRequest);
        return new ApiResponse(discount, "Create discount successfully", HttpStatus.CREATED.value());
    }

    @PutMapping("/activate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse activateDiscount(@PathVariable("id") Long id) {
        return new ApiResponse(discountService.activateDiscount(id), "Activate discount", HttpStatus.OK.value());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateDiscount(
            @PathVariable("id") Long id,
            @RequestBody @Valid DiscountRequest discountRequest
    ) {
        return new ApiResponse(discountService.updateDiscount(id, discountRequest), "Update discount", HttpStatus.OK.value());
    }

    @GetMapping
    public ApiResponse getAllDiscount(
            @RequestParam(value = "page", defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = PageConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "total", defaultValue = "0", required = false) long total,
            @CurrentUser CustomUserDetails user
    ) {
        Pageable pageable = PageableUtils.getPageable(page, size);
        PagedResponse<Discount> discounts = discountService.getAllDiscount(pageable, user, total);
        return new ApiResponse(discounts, "Get all discount", HttpStatus.OK.value());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deleteDiscount(@PathVariable("id") Long id) {
        discountService.deleteDiscount(id);
        return new ApiResponse(null, "delete successfully", HttpStatus.NO_CONTENT.value());
    }
}
