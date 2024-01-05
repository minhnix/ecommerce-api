package com.nix.ecommerceapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Order order;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Column(name = "payment_id", unique = true)
    private String paymentId;
    private String transactionDate;
    private String transactionId;
    @Column(nullable = false)
    private Double amount;

    public enum PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED
    }

    public enum PaymentMethod {
        COD,
        TRANSFER,
    }

    public enum PaymentType {
        ZALO,
        MOMO,
        VNPAY
    }
}
