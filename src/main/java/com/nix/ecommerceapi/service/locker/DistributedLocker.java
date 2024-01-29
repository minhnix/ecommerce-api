package com.nix.ecommerceapi.service.locker;

import java.util.concurrent.Callable;

public interface DistributedLocker {
    <T> LockExecutionResult<T> lock(final String key, final int maxRetryNumber, final long lockTimeOut, final Callable<T> task);
    void releaseLock(final String key);
}
