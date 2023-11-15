package com.nix.ecommerceapi.mapper;

import com.nix.ecommerceapi.model.entity.Cart;
import com.nix.ecommerceapi.model.response.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);
    @Mapping(source = "")
    CartResponse toCartResponse(Cart cart);
}
