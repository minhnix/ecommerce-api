package com.nix.ecommerceapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "carts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "model_id"}),
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends AbstractAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;
    private Long quantity;
}
