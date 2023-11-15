package com.nix.ecommerceapi.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantDefinition {
    @NotNull
    private Long optionId;
    private String optionName;
    @NotNull
    private String value;
}
