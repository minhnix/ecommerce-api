package com.nix.ecommerceapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "models", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "model_combined_name"}),
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Model extends AbstractAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(name = "model_price", nullable = false)
    private Long price;
    @Column(name = "model_price_after_discount")
    private Long priceAfterDiscount;
    @Column(name = "model_combined_name")
    private String name;
    @OneToOne(mappedBy = "model", fetch = FetchType.LAZY)
    private Inventory inventory;
    @JsonIgnore
    @Column(name = "model_is_published")
    private boolean isPublished = true;
}
