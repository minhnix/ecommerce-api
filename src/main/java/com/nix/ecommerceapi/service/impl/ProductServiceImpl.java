package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.exception.DuplicateEntityException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.mapper.ProductMapper;
import com.nix.ecommerceapi.model.entity.*;
import com.nix.ecommerceapi.model.request.ProductRequest;
import com.nix.ecommerceapi.model.request.VariantRequest;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.model.response.ProductDetailResponse;
import com.nix.ecommerceapi.model.response.SimpleProductResponse;
import com.nix.ecommerceapi.repository.CategoryRepository;
import com.nix.ecommerceapi.repository.InventoryProductRepository;
import com.nix.ecommerceapi.repository.ProductOptionRepository;
import com.nix.ecommerceapi.repository.ProductRepository;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.*;
import com.nix.ecommerceapi.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final InventoryProductService inventoryProductService;
    private final InventoryProductRepository inventoryProductRepository;
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
            if (productRequest.getOptions().isEmpty()) {
                throw new BadRequestException("Product options not set!");
            }
            productOptionService.batchInsertProductOption(savedProduct.getId(), productRequest.getOptions());
            long totalStock = insertModelAndInventoryAndGetTotalStock(savedProduct, productRequest.getVariants());
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
        Product product = productRepository.findById(id, ProductEntityGraph.____().category().____.____())
                .orElseThrow(() -> new NotFoundException(String.format("Product not found with id %d", id)));
        product.setPublished(true);
        return ProductMapper.INSTANCE.toSimpleProductResponse(productRepository.save(product));
    }

    @Transactional
    public SimpleProductResponse unPublishProduct(Long id) {
        Product product = productRepository.findById(id, ProductEntityGraph.____().category().____.____())
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
                .category()
                .____.inventoryProduct()
                .____.____()
        ).orElseThrow(() -> new NotFoundException(String.format("Product not found with id %d", id)));
        if (!product.isPublished() && (user == null || !user.isAdmin())) {
            throw new NotFoundException(String.format("Product not found with id %d", id));
        }
        List<ProductOption> productOptions = productOptionService.findAllByProduct(product);
        List<Model> models = modelService.findAllByProductId(product.getId(), true);
        ProductDetailResponse.ProductDetailResponseBuilder builder = ProductDetailResponse.builder();
        builder.setProduct(product)
                .setProductOptions(productOptions)
                .setModels(models);
        log.info(product.getAttributes());
        return builder.build();
    }

    @Override
    @Transactional
    public SimpleProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId, ProductEntityGraph.____().category().____.____())
                .orElseThrow(() -> new NotFoundException(String.format("Product not found with id %d", productId)));
        if (product.isVariant() && productRequest.isVariant()) {
            List<ProductOption> productOptions = productOptionService.findAllByProduct(product);
            List<Model> models = modelService.findAllByProductId(productId, false);
            // update model
        } else if (product.isVariant()) {
            modelService.deleteByProductId(productId);
            productOptionService.deleteAllByProductId(productId);
            Model model = modelService.createModel(product, null);
            inventoryService.createInventory(model, productRequest.getQuantity() == null ? 0L : productRequest.getQuantity());
            inventoryProductService.updateInventoryProduct(product.getId(),
                    productRequest.getQuantity() == null ? 0L : productRequest.getQuantity(),
                    null, null);
        } else if (productRequest.isVariant()) {
            if (productRequest.getOptions().isEmpty()) {
                throw new BadRequestException("Product options not set!");
            }
            //TODO: Check name valid with option
            productOptionService.batchInsertProductOption(product.getId(), productRequest.getOptions());
            modelService.deleteByProductId(productId); // delete default model if no variant
            long totalStock = insertModelAndInventoryAndGetTotalStock(product, productRequest.getVariants());
            inventoryProductService.updateInventoryProduct(productId, totalStock, null, null);
        }
        updateBasicInfoProduct(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.toSimpleProductResponse(savedProduct);
    }

    private Long insertModelAndInventoryAndGetTotalStock(Product product, List<VariantRequest> variantRequests) {
        long totalStock = 0;
        for (var variant : variantRequests) {
            Model model = modelService.createModel(product, variant);
            long inventoryStock = variant.getQuantity() != null ? variant.getQuantity() : 0;
            inventoryService.createInventory(model, inventoryStock);
            totalStock += inventoryStock;
        }
        return totalStock;
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        modelService.deleteByProductId(productId);
        productOptionService.deleteAllByProductId(productId);
        inventoryProductRepository.deleteByProductId(productId);
        productRepository.deleteById(productId);
    }

    //TODO: update partial, put product, delete.
    private void setBasicInfoProduct(Product product, ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException(String.format("Category not found with id %d", productRequest.getCategoryId())));
        boolean existName = productRepository.existsByName(productRequest.getName());
        if (existName) throw new DuplicateEntityException("Product with the same name already exists");
        product.setCategory(category);
        ProductMapper.INSTANCE.updateProduct(productRequest, product);
        product.setAttributes(JsonUtils.objectToJsonString(productRequest.getAttributes()));
    }

    private void updateBasicInfoProduct(Product product, ProductRequest productRequest) {
        if (!product.getName().equals(productRequest.getName())) {
            boolean existName = productRepository.existsByName(productRequest.getName());
            if (existName) throw new DuplicateEntityException("Product with the same name already exists");
        }
        if (!product.getCategory().getId().equals(productRequest.getCategoryId())) {
            Category category = categoryRepository.findById(productRequest.getCategoryId())
                    .orElseThrow(() -> new NotFoundException(String.format("Category not found with id %d", productRequest.getCategoryId())));
            product.setCategory(category);
        }
        ProductMapper.INSTANCE.updateProduct(productRequest, product);
        product.setAttributes(JsonUtils.objectToJsonString(productRequest.getAttributes()));
    }
}
