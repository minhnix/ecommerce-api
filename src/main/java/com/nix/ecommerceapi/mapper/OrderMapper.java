package com.nix.ecommerceapi.mapper;

import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.response.OrderSummary;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    OrderSummary mapToOrderSummary(Order order);
}
