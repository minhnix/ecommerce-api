package com.nix.ecommerceapi.security;

import com.nix.ecommerceapi.model.entity.User;
import com.nix.ecommerceapi.model.entity.UserEntityGraph;
import com.nix.ecommerceapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username, UserEntityGraph.____().roles().____.____())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " +  username));
        return CustomUserDetails.create(user);
    }
}
