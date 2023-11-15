package com.nix.ecommerceapi.config;

import com.nix.ecommerceapi.model.constants.AppConstants;
import com.nix.ecommerceapi.model.constants.PageConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new SpringSecurityAuditAware();
    }
}
class SpringSecurityAuditAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of(AppConstants.SYSTEM);
        }

//        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

//        return Optional.ofNullable(userPrincipal.getId());
        return Optional.of(1L);
    }
}