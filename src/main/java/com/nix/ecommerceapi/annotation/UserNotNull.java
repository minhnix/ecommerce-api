package com.nix.ecommerceapi.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("principal != null")
public @interface UserNotNull {
}
