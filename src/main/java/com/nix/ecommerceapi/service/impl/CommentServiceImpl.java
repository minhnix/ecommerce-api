package com.nix.ecommerceapi.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.mapper.CommentMapper;
import com.nix.ecommerceapi.model.dto.CommentDTO;
import com.nix.ecommerceapi.model.entity.Comment;
import com.nix.ecommerceapi.model.request.CommentRequest;
import com.nix.ecommerceapi.repository.CommentRepository;
import com.nix.ecommerceapi.repository.ProductRepository;
import com.nix.ecommerceapi.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public CommentDTO createComment(CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .user(commentRequest.getUser())
                .parentId(commentRequest.getParentId())
                .product(productRepository.getReferenceById(commentRequest.getProductId()))
                .build();
        long newCommentLeft;
        if (commentRequest.getParentId() == null) {
            Long maxRightValue = commentRepository.getMaxRightValue(commentRequest.getProductId());
            newCommentLeft = (maxRightValue != null) ? maxRightValue + 1 : 1L;
        } else {
            Comment parrentComment = commentRepository.findById(commentRequest.getParentId(), EntityGraph.NOOP)
                    .orElseThrow(() -> new NotFoundException("Parent comment not found with id " + commentRequest.getParentId()));
            newCommentLeft = parrentComment.getRight();
            commentRepository.updateRight(parrentComment.getProduct().getId(), parrentComment.getRight());
            commentRepository.updateLeft(parrentComment.getProduct().getId(), parrentComment.getRight());
        }
        comment.setLeft(newCommentLeft);
        comment.setRight(newCommentLeft + 1);
        return CommentMapper.INSTANCE.mapToCommentDTO(commentRepository.save(comment));
    }

    @Override
    public List<CommentDTO> getCommentByParentId(Long productId, Long parentId, Pageable pageable) {
        if (parentId != null) {
            Comment parrentComment = commentRepository.findById(parentId, EntityGraph.NOOP)
                    .orElseThrow(() -> new NotFoundException("Parent comment not found with id " + parentId));
            List<Comment> comments = commentRepository.findByParentId(productId,
                    parrentComment.getLeft(),
                    parrentComment.getRight(),
                    pageable
            );
            return comments.stream().map(CommentMapper.INSTANCE::mapToCommentDTO).collect(Collectors.toList());
        }

        List<Comment> comments = commentRepository.findByParentIdIsNull(productId, pageable);
        return comments.stream().map(CommentMapper.INSTANCE::mapToCommentDTO).collect(Collectors.toList());
    }

}
