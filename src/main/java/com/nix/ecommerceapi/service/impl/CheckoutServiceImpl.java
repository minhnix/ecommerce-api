package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.dto.ItemProduct;
import com.nix.ecommerceapi.model.entity.Inventory;
import com.nix.ecommerceapi.model.request.CheckoutRequest;
import com.nix.ecommerceapi.model.response.CartResponse;
import com.nix.ecommerceapi.model.response.CheckoutResponse;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartService cartService;
    private final DiscountService discountService;
    private final RedisService redisService;
    private final InventoryService inventoryService;

    @Override
    public CheckoutResponse checkoutReview(CheckoutRequest checkoutRequest, CustomUserDetails user) {
        List<CartResponse> cartResponses = cartService.getAllCartByUser(user);
        if (cartResponses.isEmpty()) {
            throw new NotFoundException("Cart is empty");
        }
        List<CartResponse> products = checkAndGetProductAvailable(checkoutRequest.getItemProducts(), cartResponses);
        CheckoutResponse checkoutResponse = new CheckoutResponse();
        checkoutResponse.setProducts(products);
        checkoutResponse.calculateTotalPrice();
        checkoutResponse.setTotalDiscount(discountService.applyDiscount(checkoutRequest.getDiscountId(), user, checkoutResponse.getTotalPrice()));
        checkoutResponse.setDiscountIdUsed(checkoutRequest.getDiscountId());
        checkoutResponse.setTotalShippingCost(calculateShippingCost());
        checkoutResponse.calculateTotalCheckoutPrice();
        return checkoutResponse;
    }

    @Override
    @Transactional(rollbackFor = {BadRequestException.class, NotFoundException.class, RuntimeException.class,})
    public void order(CheckoutRequest checkoutRequest, CustomUserDetails user) {
        CheckoutResponse checkoutResponse = checkoutReview(checkoutRequest, user);
        checkoutResponse.getProducts().forEach(
                cartResponse -> {
                    int maxAttempts = 10;
                    long sleepDuration = 50;
                    Boolean result = false;
                    String keyLock = "Lock:Inventory:" + cartResponse.getProduct().getModelId();
                    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                        result = redisService.acquireLock(keyLock, 1500);
                        if (result) {
                            Inventory inventory = inventoryService.processOrderAndSaveInventory(cartResponse.getProduct().getModelId(), cartResponse.getQuantity());
                            redisService.releaseLock(keyLock);
                            if (inventory == null) {
                                throw new BadRequestException("Order failed");
                            }
                            break;
                        } else {
                            waitForDuration(sleepDuration);
                        }
                    }

                    if (!result) {
                        throw new BadRequestException("Order failed");
                    }
                }
        );
    }

    private void waitForDuration(long sleepDuration) {
        try {
            Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<CartResponse> checkAndGetProductAvailable(List<ItemProduct> itemProducts, List<CartResponse> cartResponses) {
        if (itemProducts.isEmpty()) throw new BadRequestException("Order wrong");
        return itemProducts.stream().map(
                itemProduct -> {
                    CartResponse cartResponse = cartResponses.stream().filter(
                            item -> item.getProduct().getModelId().equals(itemProduct.getModelId())
                    ).findFirst().orElseThrow(() -> new BadRequestException("Order wrong"));
                    if (!itemProduct.getQuantity().equals(cartResponse.getQuantity()))
                        throw new BadRequestException("Order wrong");
                    if (itemProduct.getQuantity() > cartResponse.getProduct().getStock())
                        throw new BadRequestException("Insufficient stock");
                    return cartResponse;
                }
        ).toList();
    }

    private Double calculateShippingCost() {
        return 0.0;
    }
}
