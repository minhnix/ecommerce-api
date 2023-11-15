package com.nix.ecommerceapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "inventories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory extends AbstractAuditing  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;
    @Column(name = "inventory_stock")
    private Long stock;
    @Column(name = "total_sold")
    private Long totalSold;
}
