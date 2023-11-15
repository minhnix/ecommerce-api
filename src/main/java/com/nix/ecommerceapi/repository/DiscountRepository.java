package com.nix.ecommerceapi.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.nix.ecommerceapi.model.entity.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DiscountRepository extends EntityGraphJpaRepository<Discount, Long> {
    Optional<Discount> findByCode(String code);
    boolean existsByCode(String code);
    Page<Discount> findAllByActive(Pageable pageable, boolean isActive);
    @Query("select d from Discount d where d.startDate <= NOW() and d.endDate >= NOW() " +
            "and d.isActive = true and d.usedCount < d.maximumTotalUsage " +
            "order by case when :total >= d.minOrderValue then 0 else 1 end, " +
            "case when d.type = 'FIXED_AMOUNT' then (:total - d.value) " +
            "     when d.type = 'PERCENTAGE' then (:total - :total * d.value / 100)" +
            "     else 0 " +
            "end asc")
    Page<Discount> getAllDiscountMatchTotalOrderValue(Pageable pageable, @Param("total") long total);
}
