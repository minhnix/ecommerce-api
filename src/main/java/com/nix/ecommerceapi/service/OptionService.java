package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.Option;

import java.util.List;

public interface OptionService {
    Option createOption(Option option);
    Option updateOption(Long id, String newName);
    List<Option> findAllOption();
    void deleteOption(Long id);
}
