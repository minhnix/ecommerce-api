package com.nix.ecommerceapi.service.search;

import com.nix.ecommerceapi.model.response.PagedResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductSearchServiceImpl implements ProductSearchService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PagedResponse<ProductSearchResponse> searchProduct(ProductSearchRequest productSearchRequest, Pageable pageable) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("""
                WITH temp_product AS (
                    SELECT product_id, product_name, product_description, product_slug,
                    product_image, product_price, product_price_after_discount, product_is_published,
                    category_id,
                    MATCH(product_name, product_description) AGAINST (:keyword WITH QUERY EXPANSION) as relevancy
                    FROM products
                    WHERE MATCH(product_name, product_description) AGAINST (:keyword WITH QUERY EXPANSION)
                )
                SELECT t.*, inventory_stock, total_sold from temp_product t
                join inventory_products i on t.product_id = i.product_product_id
                where t.product_is_published = true               
                """);

        appendSearchFilter(sqlBuilder, productSearchRequest);
        appendSortBy(sqlBuilder, pageable.getSort());
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        setQueryParameters(query, productSearchRequest, pageable);
        List<Object[]> results = query.getResultList();

        List<ProductSearchResponse> responses = results.stream()
                .map(this::mapToSearchResponse)
                .toList();
        PagedResponse<ProductSearchResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(responses);
        pagedResponse.setSize(responses.size());
        pagedResponse.setPage(pageable.getPageNumber());
        return pagedResponse;
    }

    private ProductSearchResponse mapToSearchResponse(Object[] result) {
        ProductSearchResponse productSearchResponse = new ProductSearchResponse();
        productSearchResponse.setProductId((Long) result[0]);
        productSearchResponse.setProductName((String) result[1]);
        productSearchResponse.setProductDescription((String) result[2]);
        productSearchResponse.setProductSlug((String) result[3]);
        productSearchResponse.setProductImage((String) result[4]);
        productSearchResponse.setProductPrice((Long) result[5]);
        productSearchResponse.setProductPriceAfterDiscount((Long) result[6]);
        productSearchResponse.setCategoryId((Long) result[8]);
        productSearchResponse.setInventoryStock((Long) result[10]);
        productSearchResponse.setTotalSold((Long) result[11]);
        return productSearchResponse;
    }

    private void appendSearchFilter(StringBuilder sqlBuilder, ProductSearchRequest productSearchRequest) {
        if (productSearchRequest.getCategoryId() != null) {
            sqlBuilder.append(" AND t.category_id = :categoryId");
        }
        if (productSearchRequest.getMinPrice() != null) {
            sqlBuilder.append(" AND t.product_price_after_discount >= :minPrice");
        }
        if (productSearchRequest.getMaxPrice() != null) {
            sqlBuilder.append(" AND t.product_price_after_discount <= :maxPrice");
        }
    }

    private void setQueryParameters(Query query, ProductSearchRequest productSearchRequest, Pageable pageable) {
        if (productSearchRequest.getCategoryId() != null) {
            query.setParameter("categoryId", productSearchRequest.getCategoryId());
        }
        if (productSearchRequest.getMinPrice() != null) {
            query.setParameter("minPrice", productSearchRequest.getMinPrice());
        }
        if (productSearchRequest.getMaxPrice() != null) {
            query.setParameter("maxPrice", productSearchRequest.getMaxPrice());
        }
        query.setParameter("keyword", productSearchRequest.getKeyword());

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }


    private void appendSortBy(StringBuilder sqlBuilder, Sort sort) {
         Sort.Order order = sort.get()
                 .filter(i -> checkSortBy(i.getProperty()))
                 .map(this::mapOrder)
                 .findFirst()
                 .orElse(new Sort.Order(Sort.Direction.DESC, "relevancy"));
         sqlBuilder.append(" ORDER BY ").append(order.getProperty()).append(" ").append(order.getDirection());
    }

    private Sort.Order mapOrder(Sort.Order order) {
        return switch (order.getProperty()) {
            case "sales" -> new Sort.Order(Sort.Direction.DESC, "total_sold");
            case "ctime" -> new Sort.Order(Sort.Direction.DESC, "created_at");
            case "price" -> new Sort.Order(order.getDirection(), "product_price_after_discount");
            default -> new Sort.Order(Sort.Direction.DESC, "relevancy");
        };
    }

    private boolean checkSortBy(String sortBy) {
        String lowerSortBy = sortBy.toLowerCase();
        return lowerSortBy.equals("sales") || lowerSortBy.equals("ctime") || lowerSortBy.equals("relevancy") || lowerSortBy.equals("price");
    }
}
