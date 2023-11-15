package com.nix.ecommerceapi.mapper;

import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.response.SimpleProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    SimpleProductResponse toSimpleProductResponse(Product product);
}
