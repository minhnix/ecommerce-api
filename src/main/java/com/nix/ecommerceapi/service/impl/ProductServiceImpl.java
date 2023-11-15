package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.DuplicateEntityException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.mapper.ProductMapper;
import com.nix.ecommerceapi.model.dto.AttributeDTO;
import com.nix.ecommerceapi.model.entity.*;
import com.nix.ecommerceapi.model.request.ProductRequest;
import com.nix.ecommerceapi.model.response.*;
import com.nix.ecommerceapi.repository.CategoryRepository;
import com.nix.ecommerceapi.repository.ProductRepository;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final InventoryProductService inventoryProductService;
    private final InventoryService inventoryService;
    private final ModelService modelService;
    private final ProductOptionService productOptionService;

    @Override
    @Transactional
    public Product createProduct(ProductRequest productRequest) {
        log.info("Create Product");
        Product product = new Product();
        setBasicInfoProduct(product, productRequest);
        Product savedProduct = productRepository.save(product);
        if (productRequest.isVariant() && !productRequest.getVariants().isEmpty()) {
            productOptionService.batchInsertProductOption(savedProduct.getId(), productRequest.getOptions());
            long totalStock = 0;
            for (var variant : productRequest.getVariants()) {
                Model model = modelService.createModel(savedProduct, variant);
                long inventoryStock = variant.getQuantity() != null ? variant.getQuantity() : 0;
                inventoryService.createInventory(model, inventoryStock);
                totalStock += inventoryStock;
            }
            inventoryProductService.createInventoryProduct(savedProduct, totalStock);
        } else {
            inventoryProductService.createInventoryProduct(savedProduct, productRequest.getQuantity());
            Model model = modelService.createModel(savedProduct, null);
            inventoryService.createInventory(model, productRequest.getQuantity());
        }
        return savedProduct;
    }

    @Transactional
    public SimpleProductResponse publishProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Product not found with id %d", id)));
        product.setPublished(true);
        return ProductMapper.INSTANCE.toSimpleProductResponse(productRepository.save(product));
    }

    @Transactional
    public SimpleProductResponse unPublishProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Product not found with id %d", id)));
        product.setPublished(false);
        return ProductMapper.INSTANCE.toSimpleProductResponse(productRepository.save(product));
    }

    public PagedResponse<SimpleProductResponse> findAllPublishProduct(Pageable pageable) {
        Page<SimpleProductResponse> products = productRepository.findAllByPublished(pageable, true)
                .map(ProductMapper.INSTANCE::toSimpleProductResponse);
        return new PagedResponse<>(products);

    }

    public PagedResponse<SimpleProductResponse> findAllUnPublishProduct(Pageable pageable) {
        Page<SimpleProductResponse> products = productRepository.findAllByPublished(pageable, false)
                .map(ProductMapper.INSTANCE::toSimpleProductResponse);
        return new PagedResponse<>(products);

    }

    @Override
    public ProductDetailResponse findOneProduct(Long id, CustomUserDetails user) {
        Product product = productRepository.findById(id, ProductEntityGraph.____()
                .attributes()
                .____.category()
                .____.inventoryProduct()
                .____.____()
        ).orElseThrow(() -> new NotFoundException(String.format("Product not found with id %d", id)));
        if (!product.isPublished() && (user == null || !user.isAdmin())) {
            throw new NotFoundException(String.format("Product not found with id %d", id));
        }
        List<ProductOption> productOptions = productOptionService.findAllByProduct(product);
        List<Model> models = modelService.findAllByProductId(product.getId());
        ProductDetailResponse.ProductDetailResponseBuilder builder = ProductDetailResponse.builder();
        builder.setProduct(product)
                .setProductOptions(productOptions)
                .setModels(models);
        return builder.build();
    }

    @Override
    public Product updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(String.format("Product not found with id %d", productId)));
        setBasicInfoProduct(product, productRequest);

        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    //TODO: update partial, put product, delete.
    private void setBasicInfoProduct(Product product, ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException(String.format("Category not found with id %d", productRequest.getCategoryId())));
        boolean existName = productRepository.existsByName(productRequest.getName());
        if (existName) throw new DuplicateEntityException("Product with the same name already exists");
        product.setCategory(category);
        ProductMapper.INSTANCE.updateProduct(productRequest, product);
        for (AttributeDTO dto : productRequest.getAttributes()) {
            product.addAttribute(new Attribute(dto.getName(), dto.getValue()));
        }
    }

}
