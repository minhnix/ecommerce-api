package com.nix.ecommerceapi.mapper;

import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.request.ProductRequest;
import com.nix.ecommerceapi.model.response.SimpleProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    SimpleProductResponse toSimpleProductResponse(Product product);
    @Mapping(target = "attributes", ignore = true)
    Product updateProduct(ProductRequest productRequest, @MappingTarget Product product);
}
