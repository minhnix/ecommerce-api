package com.nix.ecommerceapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Option extends AbstractAuditing  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;
    @Column(name = "option_name", nullable = false, unique = true)
    private String name;
}
