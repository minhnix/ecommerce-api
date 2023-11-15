package com.nix.ecommerceapi.security.jwt;

import com.nix.ecommerceapi.model.entity.Role;
import lombok.*;


import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtPayload  {

    private Long userId;
    private String email;
    private Set<Role> roles;
}
