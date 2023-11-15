package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.mapper.DiscountMapper;
import com.nix.ecommerceapi.model.entity.Discount;
import com.nix.ecommerceapi.model.request.DiscountRequest;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.repository.DiscountRepository;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;

    @Override
    @Transactional
    public Discount createDiscount(DiscountRequest discountRequest) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(discountRequest.getEndDate())) throw new BadRequestException("Discount has expired");
        if (discountRepository.existsByCode(discountRequest.getCode()))
            throw new BadRequestException("Discount exists");
        Discount discount = DiscountMapper.INSTANCE.mapDiscountRequestToDiscount(discountRequest);
        return discountRepository.save(discount);
    }

    @Override
    public PagedResponse<Discount> getAllDiscount(Pageable pageable, CustomUserDetails user, long totalOrderValue) {
        //TODO: display discount available
        if (user != null && user.isAdmin()) {
            return new PagedResponse<>(discountRepository.findAll(pageable));
        }
        return new PagedResponse<>(discountRepository.getAllDiscountMatchTotalOrderValue(pageable, totalOrderValue));
    }

    @Override
    @Transactional
    public Discount activateDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Discount not found with id " +  id));
        discount.setActive(true);
        return discountRepository.save(discount);
    }

    @Override
    @Transactional
    public Discount updateDiscount(Long id, DiscountRequest discountRequest) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Discount not found with id " +  id));
        DiscountMapper.INSTANCE.updateDiscountFromRequest(discountRequest, discount);
        discountRepository.save(discount);
        return null;
    }

    @Override
    @Transactional
    public void deleteDiscount(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Discount not found with id " +  id));
        discountRepository.delete(discount);
    }

    @Override
    public void applyDiscount(String code, CustomUserDetails user) {

    }

    @Override
    public void cancelDiscount(String code, CustomUserDetails user) {

    }
}
