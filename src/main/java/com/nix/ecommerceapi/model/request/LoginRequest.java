package com.nix.ecommerceapi.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginRequest {
    private String email;
    private String password;
}
