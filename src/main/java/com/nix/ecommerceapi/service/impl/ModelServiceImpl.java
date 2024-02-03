package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.model.entity.Model;
import com.nix.ecommerceapi.model.entity.ModelEntityGraph;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.request.VariantRequest;
import com.nix.ecommerceapi.repository.InventoryRepository;
import com.nix.ecommerceapi.repository.ModelRepository;
import com.nix.ecommerceapi.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelServiceImpl implements ModelService {
    private final ModelRepository modelRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public Model createModel(Product product, VariantRequest variant) {
        Model model = new Model();
        model.setProduct(product);
        if (variant == null) {
            model.setName("");
            model.setPublished(true);
            model.setPrice(product.getPrice());
            model.setPriceAfterDiscount(product.getPriceAfterDiscount());
        } else {
            model.setName(variant.getName());
            model.setPrice(variant.getPrice() != null ? variant.getPrice() : product.getPrice());
            model.setPriceAfterDiscount(variant.getPriceAfterDiscount() != null ? variant.getPriceAfterDiscount() : product.getPriceAfterDiscount());
            model.setPublished(variant.isPublished());
        }
        return modelRepository.save(model);
    }

    @Override
    public List<Model> findAllByProductId(Long productId, Boolean isFetchInventory) {
        if (isFetchInventory)
            return modelRepository.findAllByProductId(productId, ModelEntityGraph.____()
                    .inventory().____.____());
        return modelRepository.findAllByProductId(productId, ModelEntityGraph.NOOP);
    }

    @Override
    @Transactional
    public void deleteByProductId(Long productId) {
        inventoryRepository.deleteAllByProductId(productId);
        modelRepository.deleteAllByProductId(productId);
    }
}
