package com.nix.ecommerceapi.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "inventories")
@Data
public class Inventory extends AbstractAuditing  {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Model model;
    @Column(name = "inventory_stock")
    private Long stock;
    @Column(name = "total_sold")
    private Long totalSold;
}
