package com.nix.ecommerceapi.mapper;

import com.nix.ecommerceapi.model.entity.Cart;
import com.nix.ecommerceapi.model.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ModelMapper.class})
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);
    @Mapping(target = "product", source = "source.model")
    @Mapping(target = "totalCost", expression = "java(calculateTotalCost(source))")
    @Mapping(target = "deleted", ignore = true)
    CartResponse toCartResponse(Cart source);

    default Long calculateTotalCost(Cart cart) {
        if (cart == null) return null;
        return cart.getQuantity() * cart.getModel().getPriceAfterDiscount();
    }
}
