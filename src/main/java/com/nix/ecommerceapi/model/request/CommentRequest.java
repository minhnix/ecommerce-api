package com.nix.ecommerceapi.model.request;

import com.nix.ecommerceapi.model.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private Long parentId;
    @NotNull
    private String content;
    @NotNull
    private Long productId;
    private User user;
}
