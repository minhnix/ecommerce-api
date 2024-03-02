package com.nix.ecommerceapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private Long parentId;
    private String content;
    private Long productId;
    private Long userId;
    private Long left;
    private Long right;
}
