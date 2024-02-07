package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.event.InventoryProductUpdateEvent;
import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.mapper.OrderMapper;
import com.nix.ecommerceapi.model.dto.ItemProduct;
import com.nix.ecommerceapi.model.dto.OrderInfo;
import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.entity.Payment;
import com.nix.ecommerceapi.model.request.CheckoutRequest;
import com.nix.ecommerceapi.model.response.CartResponse;
import com.nix.ecommerceapi.model.response.CheckoutResponse;
import com.nix.ecommerceapi.model.response.OrderSummary;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.lock.PessimisticEntityLockException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartService cartService;
    private final DiscountService discountService;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final ApplicationEventPublisher applicationEventPublisher;


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
    @Transactional(rollbackFor = {BadRequestException.class, NotFoundException.class, RuntimeException.class})
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
        try {
            checkoutResponse.getProducts().forEach(
                    cartResponse -> inventoryService.processOrderAndSaveInventory(
                            cartResponse.getProduct().getModelId(),
                            cartResponse.getQuantity())
            );
        } catch (PessimisticEntityLockException e) {
            throw new BadRequestException("Order error");
            //TODO:: change strategy handle pessimistic lock timeout (such as retry, go next product, etc...)
        }
        OrderInfo orderInfo = OrderInfo.builder()
                .address(checkoutRequest.getAddress())
                .paymentType(checkoutRequest.getPaymentType())
                .paymentMethod(checkoutRequest.getPaymentMethod())
                .userId(user.getId())
                .build();
        Order order = orderService.createOrder(checkoutResponse, orderInfo);
        checkoutResponse.getProducts().forEach(cartResponse -> cartService.deleteCart(cartResponse.getId(), user));
        this.publishEventUpdateInventoryProduct(checkoutResponse);
        return OrderMapper.INSTANCE.mapToOrderSummary(order);
    }

    private void publishEventUpdateInventoryProduct(CheckoutResponse checkoutResponse) {
        Map<Long, Long> amount = new HashMap<>();
        checkoutResponse.getProducts().forEach(
                cartResponse -> {
                    long value = amount.getOrDefault(cartResponse.getProduct().getProductId(), 0L);
                    amount.put(cartResponse.getProduct().getProductId(), value + cartResponse.getQuantity());
                }
        );
        amount.forEach((k, v) -> applicationEventPublisher.publishEvent(new InventoryProductUpdateEvent(k, v)));
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
