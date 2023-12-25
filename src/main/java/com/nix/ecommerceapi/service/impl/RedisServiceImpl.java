package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    public Boolean acquireLock(String lockName, long expireTime) {
        return redisTemplate.opsForValue().setIfAbsent(lockName, "locked", expireTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean releaseLock(String lockName) {
        return redisTemplate.delete(lockName);
    }

    @Override
    public Boolean isLocked(String lockName) {
        return redisTemplate.opsForValue().get(lockName) != null;
    }
}
