package com.nix.ecommerceapi.model.response;

import com.nix.ecommerceapi.model.dto.ExtInfo;
import com.nix.ecommerceapi.model.dto.ProductOptionsDTO;
import com.nix.ecommerceapi.model.entity.*;
import com.nix.ecommerceapi.model.request.VariantDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String slug;
    private Category category;
    private String description;
    private String image;
    private Long price;
    private Long priceAfterDiscount;
    private boolean isVariant;
    private boolean isPublish;
    private Long stock;
    private Long sold;
    private List<Attribute> attributes;
    private List<ProductOptionsDTO> productOptions;
    private List<ModelResponse> models;

    public static class ProductDetailResponseBuilder {
        public ProductDetailResponseBuilder setProduct(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.slug = product.getSlug();
            this.category = product.getCategory();
            this.description = product.getDescription();
            this.image = product.getImage();
            this.price = product.getPrice();
            this.priceAfterDiscount = product.getPriceAfterDiscount();
            this.isVariant = product.isVariant();
            this.isPublish = product.isPublished();
            this.stock = product.getInventoryProduct().getStock();
            this.sold = product.getInventoryProduct().getTotalSold();
            this.attributes = product.getAttributes();
            return this;
        }

        public ProductDetailResponseBuilder setProductOptions(List<ProductOption> productOptions) {
            List<ProductOptionsDTO> productOptionsDTOS = new ArrayList<>();
            for (var productOption : productOptions) {
                if (!productOptionsDTOS.isEmpty()) {
                    ProductOptionsDTO productOptionsDTO = productOptionsDTOS.get(productOptionsDTOS.size() - 1);
                    if (productOptionsDTO.getName().equals(productOption.getOption().getName())) {
                        productOptionsDTO.getValues().add(productOption.getValue());
                    } else {
                        List<String> values = new ArrayList<>();
                        values.add(productOption.getValue());
                        ProductOptionsDTO productOptionsDTO1 = new ProductOptionsDTO(
                                productOption.getId(),
                                productOption.getOption().getName(),
                                values);
                        productOptionsDTOS.add(productOptionsDTO1);
                    }
                } else {
                    List<String> values = new ArrayList<>();
                    values.add(productOption.getValue());
                    ProductOptionsDTO productOptionsDTO = new ProductOptionsDTO(
                            productOption.getId(),
                            productOption.getOption().getName(),
                            values);
                    productOptionsDTOS.add(productOptionsDTO);
                }
            }
            this.productOptions = productOptionsDTOS;
            return this;
        }

        public ProductDetailResponseBuilder setModels(List<Model> models) {
            List<ModelResponse> modelResponses = new ArrayList<>();
            for (var model : models) {
                ModelResponse modelResponse = new ModelResponse();
                modelResponse.setProductId(model.getProduct().getId());
                modelResponse.setModelId(model.getId());
                modelResponse.setPublished(model.isPublished());
                modelResponse.setPrice(model.getPrice());
                modelResponse.setName(model.getName());
                modelResponse.setStock(model.getInventory().getStock());
                modelResponse.setPriceAfterDiscount(model.getPriceAfterDiscount());
                modelResponse.setExtInfo(new ExtInfo(
                        getProductOptionIndex(model.getName(), this.productOptions)
                ));
                modelResponses.add(modelResponse);
            }
            this.models = modelResponses;
            return this;
        }

        private List<Integer> getProductOptionIndex(String modelName, List<ProductOptionsDTO> productOptionsList) {
            List<Integer> modelIndices = new ArrayList<>();
            String[] split = modelName.split(",");
            for (int i = 0; i < split.length; i++) {
                ProductOptionsDTO currentOption = productOptionsList.get(i);
                String value = split[i];
                int index = currentOption.getValues().indexOf(value);
                modelIndices.add(index);
            }
            return modelIndices;
        }
    }

}
