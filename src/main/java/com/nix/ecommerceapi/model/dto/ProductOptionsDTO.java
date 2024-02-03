package com.nix.ecommerceapi.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionsDTO {
    @NotNull
    private Long id;
    private String name;
    @NotNull
    private List<String> values;
}
