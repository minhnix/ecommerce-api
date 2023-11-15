package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.exception.AppException;
import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.model.entity.Role;
import com.nix.ecommerceapi.model.entity.User;
import com.nix.ecommerceapi.model.enumuration.RoleName;
import com.nix.ecommerceapi.repository.RoleRepository;
import com.nix.ecommerceapi.repository.UserRepository;
import com.nix.ecommerceapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User createUser(User user, RoleName role) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        Role role1 = roleRepository.findByName(role)
                .orElseThrow(() -> new AppException("Role not set"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("ACTIVE");
        user.setRoles(Collections.singleton(role1));
        return userRepository.save(user);
    }
}
