package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.model.dto.ProductOptionsDTO;
import com.nix.ecommerceapi.model.entity.Option;
import com.nix.ecommerceapi.model.entity.Product;
import com.nix.ecommerceapi.model.entity.ProductOption;
import com.nix.ecommerceapi.model.entity.ProductOptionEntityGraph;
import com.nix.ecommerceapi.repository.OptionRepository;
import com.nix.ecommerceapi.repository.ProductOptionRepository;
import com.nix.ecommerceapi.repository.ProductRepository;
import com.nix.ecommerceapi.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductOptionServiceImpl implements ProductOptionService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    @Override
    @Transactional
    public ProductOption createProductOption(Long productId, Long optionId, String value) {
        Product product = productRepository.getReferenceById(productId);
        Option option = optionRepository.getReferenceById(optionId);
        ProductOption productOption = new ProductOption();
        productOption.setProduct(product);
        productOption.setOption(option);
        productOption.setValue(value);
        return productOptionRepository.save(productOption);
    }

    @Override
    @Transactional
    public void batchInsertProductOption(Long productId, Long optionId, List<String> values) {
        Product product = productRepository.getReferenceById(productId);
        Option option = optionRepository.getReferenceById(optionId);
        List<ProductOption> productOptions = new ArrayList<>();
        for (String value : values) {
            ProductOption productOption = new ProductOption();
            productOption.setProduct(product);
            productOption.setOption(option);
            productOption.setValue(value);
            productOptions.add(productOption);
        }
        productOptionRepository.saveAll(productOptions);
    }

    @Override
    @Transactional
    public void batchInsertProductOption(Long productId, List<ProductOptionsDTO> productOptionsDTOS) {
        List<ProductOption> productOptions = new ArrayList<>();
        Product product = productRepository.getReferenceById(productId);
        for (var ProductOptionsDTO : productOptionsDTOS) {
            Option option = optionRepository.getReferenceById(ProductOptionsDTO.getId());
            for (String value : ProductOptionsDTO.getValues()) {
                ProductOption productOption = new ProductOption();
                productOption.setProduct(product);
                productOption.setOption(option);
                productOption.setValue(value);
                productOptions.add(productOption);
            }
        }
        productOptionRepository.saveAll(productOptions);
    }

    @Override
    public List<ProductOption> findAllByProduct(Product product) {
        return productOptionRepository.findAllByProduct(product, ProductOptionEntityGraph.____().option().____.____());
    }

    @Override
    @Transactional
    public void deleteAllByProductId(Long productId) {
        productOptionRepository.deleteAllByProductId(productId);
    }
}
