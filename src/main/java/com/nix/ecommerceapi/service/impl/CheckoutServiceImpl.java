package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.dto.ItemProduct;
import com.nix.ecommerceapi.model.entity.Inventory;
import com.nix.ecommerceapi.model.entity.Order;
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
    private final OrderService orderService;

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
        if (checkoutRequest.getPayment() == null)
            throw new BadRequestException("No payment method choose");
        if (checkoutRequest.getAddress() == null || checkoutRequest.getAddress().isEmpty())
            throw new BadRequestException("Address is empty");
        CheckoutResponse checkoutResponse = checkoutReview(checkoutRequest, user);
        int maxAttempts = 10, sleepDuration = 50, expireTime = 1500;
        checkoutResponse.getProducts().forEach(
                cartResponse -> {
                    Boolean result = false;
                    String keyLock = "Lock:Inventory:" + cartResponse.getProduct().getModelId();
                    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                        result = redisService.acquireLock(keyLock, expireTime);
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

        Order order = orderService.createOrder(checkoutResponse, user, checkoutRequest.getAddress(), checkoutRequest.getPayment());
        if (order.getId() != null) {
            checkoutResponse.getProducts().forEach(
                    cartResponse -> cartService.deleteCart(cartResponse.getId(), user)
            );
        }
    }

    private void waitForDuration(long sleepDuration) {
        try {
            Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<CartResponse> checkAndGetProductAvailable(List<ItemProduct> itemProducts, List<CartResponse> cartResponses) {
        if (itemProducts.isEmpty()) throw new BadRequestException("No product to review checkout");
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
