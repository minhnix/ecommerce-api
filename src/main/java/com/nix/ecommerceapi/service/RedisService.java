package com.nix.ecommerceapi.service;

public interface RedisService {
    Boolean acquireLock(String lockName, long expireTime);
    Boolean releaseLock(String lockName);
    Boolean isLocked(String lockName);

}
