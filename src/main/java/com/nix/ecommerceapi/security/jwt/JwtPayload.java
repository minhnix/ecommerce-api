package com.nix.ecommerceapi.security.jwt;

import com.nix.ecommerceapi.model.entity.Role;
import lombok.*;


import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtPayload  {

    private Long userId;
    private String email;
    private String firstName;
    private String lastname;
    private String status;
    private Set<Role> roles;
}
