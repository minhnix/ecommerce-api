package com.nix.ecommerceapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "attributes")
@Getter
@Setter
@NoArgsConstructor
public class Attribute  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;
    @Column(name = "attribute_name", nullable = false)
    private String name;
    @Column(name = "attribute_value", nullable = false)
    private String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
