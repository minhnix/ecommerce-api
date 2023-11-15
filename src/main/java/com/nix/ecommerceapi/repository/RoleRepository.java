package com.nix.ecommerceapi.repository;

import com.nix.ecommerceapi.model.entity.Role;
import com.nix.ecommerceapi.model.enumuration.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
