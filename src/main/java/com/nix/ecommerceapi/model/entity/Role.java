package com.nix.ecommerceapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nix.ecommerceapi.model.enumuration.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.List;


@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    private RoleName name;

    private String description;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<Permission> permissions;
}
