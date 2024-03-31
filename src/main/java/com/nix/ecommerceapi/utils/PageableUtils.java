package com.nix.ecommerceapi.utils;

import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.model.constants.PageConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {
    public static void validatePage(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }
        if (size > PageConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + PageConstants.MAX_PAGE_SIZE);
        }
    }

    public static Pageable getPageable(int page, int size, String sortBy, String sortDir) {
        validatePage(page, size);
        if (sortBy == null)
            return PageRequest.of(page, size);
        if (sortDir == null)
            sortDir ="asc";
        Sort sort = (sortDir.equalsIgnoreCase(PageConstants.SORT_DES)) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(page, size, sort);
    }

    public static Pageable getPageable(int page, int size) {
        validatePage(page, size);
        return PageRequest.of(page, size);
    }
}
