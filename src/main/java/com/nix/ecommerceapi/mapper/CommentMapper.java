package com.nix.ecommerceapi.mapper;

import com.nix.ecommerceapi.model.dto.CommentDTO;
import com.nix.ecommerceapi.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
    @Mapping(target = "productId", source = "source.product.id")
    @Mapping(target = "userId", source = "source.user.id")
    CommentDTO mapToCommentDTO(Comment source);
}
