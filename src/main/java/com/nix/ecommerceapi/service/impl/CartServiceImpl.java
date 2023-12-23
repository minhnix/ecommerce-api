package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.ForbiddenException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.mapper.CartMapper;
import com.nix.ecommerceapi.model.dto.CartDTO;
import com.nix.ecommerceapi.model.entity.Cart;
import com.nix.ecommerceapi.model.entity.CartEntityGraph;
import com.nix.ecommerceapi.model.entity.Model;
import com.nix.ecommerceapi.model.request.CartRequest;
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
    public CartDTO addProductToCart(CartRequest cartRequest, CustomUserDetails user) {
        Cart cart = cartRepository.findByUserIdAndModelId(user.getId(), cartRequest.getModelId(), CartEntityGraph.NOOP)
                .orElse(null);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
        } else {
            cart = new Cart();
            cart.setUser(user.getUser());
            Model model = modelRepository.findById(cartRequest.getModelId(), CartEntityGraph.NOOP)
                    .orElseThrow(() -> new NotFoundException("Model not found with id: " + cartRequest.getModelId()));
            cart.setModel(model);
            cart.setQuantity(cartRequest.getQuantity());
        }
        Cart cart1 = cartRepository.save(cart);
        return CartMapper.INSTANCE.toCartDTO(cart1, cart1.getModel().getId(), user.getId());
    }

    @Override
    @Transactional
    public CartDTO updateCartQuantity(Long cartId, CartRequest cartRequest, CustomUserDetails user) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        if (!cart.getUser().getId().equals(user.getId()))
            throw new ForbiddenException("Access denied");
        if (cartRequest.getQuantity() == 0) {
            cartRepository.delete(cart);
            return null;
        }
        cart.setQuantity(cartRequest.getQuantity());
        Cart cart1 = cartRepository.save(cart);
        return CartMapper.INSTANCE.toCartDTO(cart1, cart1.getModel().getId(), user.getId());
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId, CustomUserDetails user) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        if (!cart.getUser().getId().equals(user.getId()))
            throw new ForbiddenException("Access denied");
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
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
