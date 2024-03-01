package com.nix.ecommerceapi.service.locker;

public record LockExecutionResult<T>(boolean lockAcquired, T resultIfLockAcquired, Exception exception) {

    public static <T> LockExecutionResult<T> buildLockAcquiredResult(final T result) {
        return new LockExecutionResult<>(true, result, null);
    }

    public static <T> LockExecutionResult<T> buildLockAcquiredWithException(final Exception exception) {
        return new LockExecutionResult<>(true, null, exception);
    }

    public static <T> LockExecutionResult<T> buildLockNotAcquiredWithException(final Exception exception) {
        return new LockExecutionResult<>(false, null, exception);
    }

}
