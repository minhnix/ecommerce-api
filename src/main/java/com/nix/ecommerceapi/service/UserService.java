package com.nix.ecommerceapi.service;

import com.nix.ecommerceapi.model.entity.User;
import com.nix.ecommerceapi.model.enumuration.RoleName;

public interface UserService {
    User createUser(User user, RoleName roles);
}
