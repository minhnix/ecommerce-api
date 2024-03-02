package com.nix.ecommerceapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "cmt_left", columnList = "lft"),
        @Index(name = "cmt_right", columnList = "rgt"),
        @Index(name = "cmt_parent_id", columnList = "parent_id"),
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends AbstractAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(name = "lft")
    private Long left;
    @Column(name = "rgt")
    private Long right;
    @Column(name = "parent_id")
    private Long parentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
