package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.ForbiddenException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.mapper.CartMapper;
import com.nix.ecommerceapi.model.entity.Cart;
import com.nix.ecommerceapi.model.entity.CartEntityGraph;
import com.nix.ecommerceapi.model.entity.Model;
import com.nix.ecommerceapi.model.request.CartRequest;
import com.nix.ecommerceapi.model.request.UpdateCartQuantity;
import com.nix.ecommerceapi.model.response.CartResponse;
import com.nix.ecommerceapi.repository.CartRepository;
import com.nix.ecommerceapi.repository.ModelRepository;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ModelRepository modelRepository;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Cart addProductToCart(CartRequest cartRequest, CustomUserDetails user) {
        Cart cart = cartRepository.findByUserIdAndModelId(user.getId(), cartRequest.getModelId())
                .orElse(null);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
        } else {
            cart = new Cart();
            cart.setUser(user.getUser());
            Model model = modelRepository.findById(cartRequest.getModelId())
                    .orElseThrow(() -> new NotFoundException("Model not found with id: " + cartRequest.getModelId()));
            cart.setModel(model);
            cart.setQuantity(cartRequest.getQuantity());
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateProductQuantity(UpdateCartQuantity updateCartQuantity, CustomUserDetails user) {
        Cart cart = cartRepository.findById(updateCartQuantity.getCartId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        if (!cart.getUser().getId().equals(user.getId()))
            throw new ForbiddenException("Access denied");
        if (updateCartQuantity.getQuantity() == 0) {
            cartRepository.delete(cart);
            return null;
        }
        cart.setQuantity(updateCartQuantity.getQuantity());
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Long cartId, CustomUserDetails user) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        if (!cart.getUser().getId().equals(user.getId()))
            throw new ForbiddenException("Access denied");
        cartRepository.delete(cart);
    }

    @Override
    public void deleteAllCart(CustomUserDetails user) {
        cartRepository.deleteByUserId(user.getId());
    }

    @Override
    public int getAmountCart(CustomUserDetails user) {
        return cartRepository.countByUserId(user.getId());
    }

    @Override
    public List<CartResponse> getAllCartByUser(CustomUserDetails user) {
        List<Cart> carts = cartRepository.findAllByUserId(user.getId(), CartEntityGraph.____()
                .model().product().____.____());
        return carts.stream().map(CartMapper.INSTANCE::toCartResponse).collect(Collectors.toList());
    }
}
