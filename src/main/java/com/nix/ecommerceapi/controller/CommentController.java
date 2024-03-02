package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.annotation.CurrentUser;
import com.nix.ecommerceapi.annotation.UserNotNull;
import com.nix.ecommerceapi.model.constants.PageConstants;
import com.nix.ecommerceapi.model.dto.CommentDTO;
import com.nix.ecommerceapi.model.request.CommentRequest;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.CommentService;
import com.nix.ecommerceapi.utils.PageableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentDTO createComment(@RequestBody CommentRequest commentRequest, @CurrentUser @UserNotNull CustomUserDetails user) {
        commentRequest.setUser(user.getUser());
        return commentService.createComment(commentRequest);
    }

    @GetMapping
    public List<CommentDTO> getComments(
            @RequestParam(value = "page", defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = PageConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "parentId", required = false) Long parentId
    ) {
        Pageable pageable = PageableUtils.getPageable(page, size);
        return commentService.getCommentByParentId(productId, parentId, pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(
            @PathVariable("id") Long commentId,
            @RequestParam(value = "productId") Long productId
    ) {
        commentService.deleteComment(productId, commentId);
    }
}
