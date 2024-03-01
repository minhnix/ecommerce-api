package com.nix.ecommerceapi.service.locker;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LockExecutionResult<T> {
    private final boolean lockAcquired;
    private final T resultIfLockAcquired;
    private final Exception exception;

    public LockExecutionResult(boolean lockAcquired, T resultIfLockAcquired, final Exception exception) {
        this.lockAcquired = lockAcquired;
        this.resultIfLockAcquired = resultIfLockAcquired;
        this.exception = exception;
    }

    public static <T> LockExecutionResult<T> buildLockAcquiredResult(final T result) {
        return new LockExecutionResult<>(true, result, null);
    }

    public static <T> LockExecutionResult<T> buildLockAcquiredWithException(final Exception exception) {
        return new LockExecutionResult<>(true, null, exception);
    }

    public static <T> LockExecutionResult<T> buildLockNotAcquiredResult() {
        return new LockExecutionResult<>(false, null, null);
    }

}
