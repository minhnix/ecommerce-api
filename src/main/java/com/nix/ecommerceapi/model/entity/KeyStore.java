package com.nix.ecommerceapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "key_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeyStore  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    @Column(columnDefinition = "TEXT")
    private String refreshToken;
}
