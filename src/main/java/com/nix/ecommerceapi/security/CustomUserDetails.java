package com.nix.ecommerceapi.security;

import com.nix.ecommerceapi.model.entity.Role;
import com.nix.ecommerceapi.model.entity.User;
import com.nix.ecommerceapi.model.enumuration.RoleName;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    @Valid
    private User user;
    public Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public static CustomUserDetails create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new CustomUserDetails(
                user,
                authorities
        );
    }
    public boolean isAdmin() {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(RoleName.ROLE_ADMIN));
    }
    public Long getId() {
        return user.getId();
    }
    public User getUser() {
        return user;
    }
    public Set<Role> getRole() {
        return user.getRoles();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId());
    }

}
