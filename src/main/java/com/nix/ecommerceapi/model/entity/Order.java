package com.nix.ecommerceapi.model.entity;

import com.nix.ecommerceapi.model.enumuration.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User user;
    private Double totalPrice;
    private Double totalDiscount;
    private Double totalShippingCost;
    private Double totalCheckoutPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String address;
    private String note;
    private String trackingNumber;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
