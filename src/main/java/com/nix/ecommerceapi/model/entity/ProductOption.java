package com.nix.ecommerceapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "product_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;
    @Column(nullable = false)
    private String value;
}
