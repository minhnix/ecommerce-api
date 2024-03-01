package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.event.InventoryProductUpdateEvent;
import com.nix.ecommerceapi.exception.AppException;
import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.mapper.OrderMapper;
import com.nix.ecommerceapi.model.dto.ItemProduct;
import com.nix.ecommerceapi.model.dto.OrderInfo;
import com.nix.ecommerceapi.model.entity.Inventory;
import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.entity.Payment;
import com.nix.ecommerceapi.model.request.CheckoutRequest;
import com.nix.ecommerceapi.model.response.CartResponse;
import com.nix.ecommerceapi.model.response.CheckoutResponse;
import com.nix.ecommerceapi.model.response.OrderSummary;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.*;
import com.nix.ecommerceapi.service.locker.DistributedLocker;
import com.nix.ecommerceapi.service.locker.LockExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartService cartService;
    private final DiscountService discountService;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DistributedLocker locker;


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
        Double totalDiscount = (checkoutRequest.getDiscountId() != null) ?
                discountService.applyDiscount(checkoutRequest.getDiscountId(), user, checkoutResponse.getTotalPrice()) : 0.0;
        checkoutResponse.setTotalDiscount(totalDiscount);
        checkoutResponse.setDiscountIdUsed(checkoutRequest.getDiscountId());
        checkoutResponse.setDiscountIdUsed(checkoutRequest.getDiscountId());
        checkoutResponse.setTotalShippingCost(calculateShippingCost());
        checkoutResponse.calculateTotalCheckoutPrice();
        return checkoutResponse;
    }

    @Override
    @Transactional(rollbackFor = {BadRequestException.class, NotFoundException.class, AppException.class, RuntimeException.class})
    public OrderSummary order(CheckoutRequest checkoutRequest, CustomUserDetails user) {
        //TODO: fixed order price with x.xx
        if (checkoutRequest.getPaymentMethod() == null)
            throw new BadRequestException("No payment method choose");
        if (checkoutRequest.getPaymentMethod() == Payment.PaymentMethod.TRANSFER &&
                checkoutRequest.getPaymentType() == null
        ) {
            throw new BadRequestException("No payment type choose");
        }
        if (checkoutRequest.getAddress() == null || checkoutRequest.getAddress().isEmpty())
            throw new BadRequestException("Address is empty");
        CheckoutResponse checkoutResponse = this.checkoutReview(checkoutRequest, user);
        String prefixKey = "ORDER:";
        checkoutResponse.getProducts().forEach(
                cartResponse -> {
                    try {
                        LockExecutionResult<Inventory> result = locker.lock(prefixKey + cartResponse.getProduct().getModelId().toString(),
                                3000,
                                2500,
                                () -> inventoryService.processOrderAndSaveInventory(cartResponse.getProduct().getModelId(), cartResponse.getQuantity()));
                        if (result.exception() != null) {
                            throw new AppException("Order wrong");
                        }
                    } catch (Exception e) {
                        throw new AppException("Order wrong");
                    }
                }
        );
        OrderInfo orderInfo = OrderInfo.builder()
                .address(checkoutRequest.getAddress())
                .paymentType(checkoutRequest.getPaymentType())
                .paymentMethod(checkoutRequest.getPaymentMethod())
                .userId(user.getId())
                .build();
        Order order = orderService.createOrder(checkoutResponse, orderInfo);
        checkoutResponse.getProducts().forEach(cartResponse -> cartService.deleteCart(cartResponse.getId(), user));
        applicationEventPublisher.publishEvent(new InventoryProductUpdateEvent(checkoutResponse.getProducts()));
        return OrderMapper.INSTANCE.mapToOrderSummary(order);
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
