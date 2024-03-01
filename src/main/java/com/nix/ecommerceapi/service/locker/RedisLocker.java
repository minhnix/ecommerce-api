package com.nix.ecommerceapi.service.locker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@Slf4j
@AllArgsConstructor
public class RedisLocker implements DistributedLocker {
    private static final int DEFAULT_RETRY_TIME_MS = 100;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public <T> LockExecutionResult<T> lock(String key, long maxRetryTimeMs, long lockTimeOut, Callable<T> task) {
        try {
            return tryToGetLock(() -> {
                final Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(key, "1", lockTimeOut, TimeUnit.MILLISECONDS);
                if (lockAcquired == Boolean.FALSE) {
                    log.info("failed to get lock for key: {}", key);
                    return null;
                }
                log.info("successfully get lock for key: {}", key);
                try {
                    T taskResult = task.call();
                    return LockExecutionResult.buildLockAcquiredResult(taskResult);
                } catch (Exception e) {
                    log.error("Error while executing task for key: {}", key, e);
                    return LockExecutionResult.buildLockAcquiredWithException(e);
                } finally {
                    releaseLock(key);
                }
            }, key, maxRetryTimeMs);
        } catch (final Exception e) {
            log.error("Error while trying to get lock for key: {}", key, e);
            return LockExecutionResult.buildLockAcquiredWithException(e);
        }
    }

    @Override
    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }

    private static <T> T tryToGetLock(final Supplier<T> task, final String key, final long maxRetryTimeMs) throws Exception {
        long currentTime = System.currentTimeMillis();
        do {
            final T response = task.get();
            if (response != null) {
                return response;
            }
            sleep(DEFAULT_RETRY_TIME_MS);
        } while (System.currentTimeMillis() <= currentTime + maxRetryTimeMs);
        throw new Exception("Max retry but cannot get lock: " + key);
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

