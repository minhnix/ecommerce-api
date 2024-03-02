package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends EntityGraphJpaRepository<Comment, Long> {
    @Query("select c.right from Comment c where c.product.id = :productId order by c.right desc limit 1")
    Long getMaxRightValue(@Param("productId") Long productId);

    Optional<Comment> findById(Long parentId, EntityGraph entityGraph);

    @Query(value = "update comments set rgt = rgt + 2 where product_id = :productId and rgt >= :rgt", nativeQuery = true)
    @Modifying
    @Transactional
    int updateRight(@Param("productId") Long productId, @Param("rgt") Long rgt);
    @Query(value = "update comments set lft = lft + 2 where product_id = :productId and lft > :rgt", nativeQuery = true)
    @Modifying
    @Transactional
    int updateLeft(@Param("productId") Long productId, @Param("rgt") Long rgt);

    @Query(value = "select c from Comment c where c.product.id = :productId and c.left > :left and c.right < :right order by c.left")
    List<Comment> findByParentId(@Param("productId") Long productId, @Param("left") Long left, @Param("right") Long right, Pageable pageable);
    @Query("select c from Comment c where c.product.id = :productId and c.parentId is null order by c.left")
    List<Comment> findByParentIdIsNull(@Param("productId") Long productId, Pageable pageable);
}
