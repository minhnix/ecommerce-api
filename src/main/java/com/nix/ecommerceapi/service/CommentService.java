package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.dto.CommentDTO;
import com.nix.ecommerceapi.model.request.CommentRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CommentRequest commentRequest);
    List<CommentDTO> getCommentByParentId(Long productId, Long parentId, Pageable pageable);
    void deleteComment(Long productId, Long commentId);
}
