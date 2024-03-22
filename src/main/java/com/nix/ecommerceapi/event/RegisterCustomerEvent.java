package com.nix.ecommerceapi.event;

import com.nix.ecommerceapi.model.dto.PreUserDTO;

public record RegisterCustomerEvent(PreUserDTO user) {}