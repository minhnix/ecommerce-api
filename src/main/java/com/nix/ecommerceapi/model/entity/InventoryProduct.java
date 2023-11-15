package com.nix.ecommerceapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "inventory_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryProduct extends AbstractAuditing  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_product_id", nullable = false)
    private Product product;
    @Column(name = "inventory_location")
    private String location;
    @Column(name = "inventory_stock")
    private Long stock;
    @Column(name = "total_sold")
    private Long totalSold;
}
