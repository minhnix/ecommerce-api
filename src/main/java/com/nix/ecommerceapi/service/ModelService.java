package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Model;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.request.VariantRequest;

import java.util.List;

public interface ModelService {
    Model createModel(Product product, VariantRequest variant);
    List<Model> findAllByProductId(Long productId);
}
