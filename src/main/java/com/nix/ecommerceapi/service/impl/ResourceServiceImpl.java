package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.entity.Resource;
import com.nix.ecommerceapi.model.response.PagedResponse;
import com.nix.ecommerceapi.repository.PermissionRepository;
import com.nix.ecommerceapi.repository.ResourceRepository;
import com.nix.ecommerceapi.service.ResourceService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Resource create(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    @Transactional
    @CacheEvict(value = "permission", allEntries = true)
    public Resource update(Resource resource) {
        Resource resource1 = resourceRepository.findById(resource.getId())
                .orElseThrow(() -> new NotFoundException("Resource not found"));
        resource1.setName(resource.getName());
        resource1.setDescription(resource.getDescription());
        return resourceRepository.save(resource1);
    }

    @Override
    @Transactional
    @CacheEvict(value = "permission", allEntries = true)
    public void delete(Long id) {
        permissionRepository.deleteALlByResourceId(id);
        resourceRepository.deleteById(id);
    }

    @Override
    public PagedResponse<Resource> findAll(Pageable pageable) {
        return new PagedResponse<>(resourceRepository.findAll(pageable));
    }

    @Override
    public Resource findById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Resource not found"));
    }
}
