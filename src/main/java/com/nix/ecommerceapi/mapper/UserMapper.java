package com.nix.ecommerceapi.mapper;

import com.nix.ecommerceapi.model.entity.User;
import com.nix.ecommerceapi.security.jwt.JwtPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(target = "id", source = "source.userId")
    User toUser(JwtPayload source);
}
