package com.nix.ecommerceapi.security.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    private String accessToken;
    private String refreshToken;
}
