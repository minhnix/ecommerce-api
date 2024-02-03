package com.nix.ecommerceapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nix.ecommerceapi.utils.SlugUtils;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "index_product_name", columnList = "product_name"),
        @Index(name = "index_product_slug", columnList = "product_slug"),
        @Index(name = "index_product_is_published", columnList = "product_is_published")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractAuditing  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "product_name", nullable = false)
    private String name;
    @Column(name = "product_slug", nullable = false, unique = true)
    private String slug;
    @Column(name = "product_description", nullable = false)
    private String description;
    @Column(name = "product_image")
    private String image;
    @Column(name = "product_price")
    private Long price;
    @Column(name = "product_price_after_discount")
    private Long priceAfterDiscount;
    @JsonIgnore
    @Column(name = "product_is_published")
    private boolean isPublished = false;
    @JsonProperty("is_variant")
    private boolean isVariant = false;
    @Type(JsonType.class)
    @Column(columnDefinition = "JSON")
    private String attributes;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "product", optional = false, cascade = CascadeType.ALL)
    private InventoryProduct inventoryProduct;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Model> models = new ArrayList<>();
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductOption> productOptions = new ArrayList<>();

    @PrePersist
    private void createSlug() {
        slug = SlugUtils.createSlug(name);
    }
}
