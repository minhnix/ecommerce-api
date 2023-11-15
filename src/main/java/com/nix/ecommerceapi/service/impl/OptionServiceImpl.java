package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.DuplicateEntityException;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.model.entity.Option;
import com.nix.ecommerceapi.repository.OptionRepository;
import com.nix.ecommerceapi.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;
    @Override
    @Transactional
    public Option createOption(Option option) {
        boolean exist = optionRepository.existsByName(option.getName());
        if (exist) throw new DuplicateEntityException("An Option with the same name already exists");
        return optionRepository.save(option);
    }

    @Override
    public Option updateOption(Long id, String newName) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Option not found with id: " + id));
        boolean exist = optionRepository.existsByName(option.getName());
        if (exist) throw new DuplicateEntityException("An Option with the same name already exists");
        option.setName(newName);
        return optionRepository.save(option);
    }

    @Override
    public List<Option> findAllOption() {
        return optionRepository.findAll();
    }

    @Override
    public void deleteOption(Long id) {
        optionRepository.deleteById(id);
    }

    public Option getOne(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Option not found with id: " + id));
    }
}
