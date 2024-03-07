package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Resource;
import com.nix.ecommerceapi.model.response.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface ResourceService {
    Resource create(Resource resource);

    Resource update(Resource resource);

    void delete(Long id);

    PagedResponse<Resource> findAll(Pageable pageable);

    Resource findById(Long id);
}
