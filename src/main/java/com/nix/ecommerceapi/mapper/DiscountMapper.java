package com.nix.ecommerceapi.mapper;

import com.nix.ecommerceapi.model.entity.Discount;
import com.nix.ecommerceapi.model.request.DiscountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DiscountMapper {
    DiscountMapper INSTANCE = Mappers.getMapper(DiscountMapper.class);

    void updateDiscountFromRequest(DiscountRequest discountRequest, @MappingTarget Discount discount);

    Discount mapDiscountRequestToDiscount(DiscountRequest discountRequest);
}
