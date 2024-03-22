package com.nix.ecommerceapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nix.ecommerceapi.event.RegisterCustomerEvent;
import com.nix.ecommerceapi.exception.AppException;
import com.nix.ecommerceapi.exception.AuthFailureException;
import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.model.dto.PreUserDTO;
import com.nix.ecommerceapi.model.entity.KeyStore;
import com.nix.ecommerceapi.model.entity.Role;
import com.nix.ecommerceapi.model.entity.User;
import com.nix.ecommerceapi.model.enumuration.RoleName;
import com.nix.ecommerceapi.model.request.LoginRequest;
import com.nix.ecommerceapi.model.request.SignUpRequest;
import com.nix.ecommerceapi.model.response.ApiResponse;
import com.nix.ecommerceapi.repository.KeyStoreRepository;
import com.nix.ecommerceapi.repository.RoleRepository;
import com.nix.ecommerceapi.repository.UserRepository;
import com.nix.ecommerceapi.security.CustomUserDetails;
import com.nix.ecommerceapi.security.jwt.JwtPayload;
import com.nix.ecommerceapi.security.jwt.JwtUtils;
import com.nix.ecommerceapi.security.jwt.Token;
import com.nix.ecommerceapi.service.AuthService;
import com.nix.ecommerceapi.service.KeyStoreService;
import com.nix.ecommerceapi.utils.JsonUtils;
import com.nix.ecommerceapi.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.nix.ecommerceapi.model.constants.AppConstants.MAIL_VERIFY_EXPIRED_TIME_MINUTES;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final KeyStoreService keyStoreService;
    private final KeyStoreRepository keyStoreRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RoleRepository roleRepository;

    @Override
    public ApiResponse registerCustomer(SignUpRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        String encodedEmail = Base64.getEncoder().encodeToString(user.getEmail().getBytes());
        String token = SecurityUtils.genarateRandomString(32) + encodedEmail;
        PreUserDTO preUserDTO = PreUserDTO.builder()
                .email(user.getEmail())
                .status("PENDING")
                .token(token)
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        String json = JsonUtils.objectToJsonString(preUserDTO);
        redisTemplate.opsForValue().set(preUserDTO.getEmail(), json, MAIL_VERIFY_EXPIRED_TIME_MINUTES, TimeUnit.MINUTES);
        applicationEventPublisher.publishEvent(new RegisterCustomerEvent(preUserDTO));
        return ApiResponse.successWithOutMetadata("Please check your email to verify your account");
    }

    @Override
    @Transactional
    public Token login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        JwtPayload payload = JwtPayload.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastname(user.getLastname())
                .status(user.getStatus())
                .roles(user.getRoles())
                .build();
        Token token = jwtUtils.generateTokenPair(payload);
        keyStoreService.updateRefreshToken(
                user.getId(),
                token.getRefreshToken(),
                null
        );
        return token;
    }

    @Override
    @Transactional
    public Token doRefreshToken(String refreshToken) {
        JwtPayload payload = jwtUtils.getPayLoadIfTokenValidated(refreshToken);
        Long userId = payload.getUserId();
        KeyStore keyStore = keyStoreRepository.findByUserId(userId)
                .orElseThrow(() -> new AuthFailureException("Invalid user"));
        if (Objects.equals(keyStore.getRefreshToken(), refreshToken)) {
            Token token = jwtUtils.generateTokenPair(payload);
            keyStore.setRefreshToken(token.getRefreshToken());
            keyStoreRepository.save(keyStore);
            return token;
        } else throw new AuthFailureException("Invalid token");
    }

    @Override
    @Transactional
    public ApiResponse verifyEmail(String token) {
        String encodedEmail = token.substring(32);
        String email = new String(Base64.getDecoder().decode(encodedEmail));
        String json = redisTemplate.opsForValue().get(email);
        if (json == null) {
            throw new BadRequestException("Invalid token or token expired");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PreUserDTO preUser = objectMapper.readValue(json, PreUserDTO.class);
            if (!token.equals(preUser.getToken())) {
                throw new BadRequestException("Invalid token");
            }
            Role role = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                    .orElseThrow(() -> new AppException("Role not set"));
            User user = new User();
            user.setPassword(preUser.getPassword());
            user.setStatus("ACTIVE");
            user.setEmail(preUser.getEmail());
            user.setRoles(Collections.singleton(role));
            User savedUser = userRepository.save(user);
            keyStoreService.createNewKeyStore(
                    savedUser.getId(),
                    null
            );

            return ApiResponse.successWithOutMetadata("Account verified successfully");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}