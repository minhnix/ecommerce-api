package com.nix.ecommerceapi.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nix.ecommerceapi.model.enumuration.DiscountType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class DiscountRequest {
    private String name;
    @NotNull
    @Valid
    private DiscountType type;
    @NotEmpty
    private String code;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    @NotNull
    private Long value;
    @NotNull
    private Long maximumTotalUsage;
    private Long discountMaxValue;
    private Long maxUsePerUser = 1L;
    private Long minOrderValue;
    private Long usedCount = 0L;
    private boolean isActive = true;
}
