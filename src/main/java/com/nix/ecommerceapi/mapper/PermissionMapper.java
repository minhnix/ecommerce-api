package com.nix.ecommerceapi.mapper;

import com.nix.ecommerceapi.model.entity.Permission;
import com.nix.ecommerceapi.model.response.PermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
    @Mapping(target = "role", ignore = true)
    PermissionResponse toPermissionResponse(Permission source);
}
