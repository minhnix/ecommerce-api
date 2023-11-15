package com.nix.ecommerceapi.model.entity;

import com.nix.ecommerceapi.model.enumuration.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "discounts", indexes = {
        @Index(name = "index_discount_code", columnList = "discount_code"),
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discount extends AbstractAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long id;
    @Column(name = "discount_name", columnDefinition = "TEXT")
    private String name;
    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType type;
    @Column(name = "discount_code", unique = true, nullable = false)
    private String code;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Column(name = "discount_value", nullable = false)
    private Long value;
    private Long discountMaxValue;
    @Column(nullable = false)
    private Long maximumTotalUsage;
    private Long maxUsePerUser;
    private Long minOrderValue;
    private Long usedCount;
    private boolean isActive;
    @Version
    private int version;
}
