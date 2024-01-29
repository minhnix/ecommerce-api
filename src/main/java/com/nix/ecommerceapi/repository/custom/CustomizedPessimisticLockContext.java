package com.nix.ecommerceapi.repository.custom;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public abstract class CustomizedPessimisticLockContext {
    @Value("${concurrency.pessimisticLocking.requiredToSetLockTimeoutForEveryQuery: true}")
    private boolean requiredToSetLockTimeoutForEveryQuery;

    // in mysql not support query hint for lock timeout
    @Getter
    @Value("${concurrency.pessimisticLocking.requiredToSetLockTimeoutQueryHint: false}")
    private boolean requiredToSetLockTimeoutQueryHint;

    @Getter
    @Value("${concurrency.pessimisticLocking.minimalPossibleLockTimeOutInMs: 1000}")
    private long minimalPossibleLockTimeOutInMs;

    @Getter
    @Value("${concurrency.pessimisticLocking.lockTimeOutInMsForQueryGetEntity: 5000}")
    private long lockTimeOutInMsForQueryGetEntity;

    protected final EntityManager em;

    protected void setLockTimeout(long timeoutDurationInMs) {
        long timeoutDurationInSec = TimeUnit.MILLISECONDS.toSeconds(timeoutDurationInMs);
        Query query = em.createNativeQuery("set session innodb_lock_wait_timeout = " + timeoutDurationInSec);
        query.executeUpdate();
    }

    protected long getLockTimeout() {
        Query query = em.createNativeQuery("select @@innodb_lock_wait_timeout");
        long timeoutDurationInSec = ((BigInteger) query.getSingleResult()).longValue();
        return TimeUnit.SECONDS.toMillis(timeoutDurationInSec);
    }

    protected Query setLockTimeoutIfRequired(Query query) {
        if (requiredToSetLockTimeoutForEveryQuery) {
            setLockTimeout(getLockTimeOutInMsForQueryGetEntity());
        }

        if (requiredToSetLockTimeoutQueryHint) {
            query.setHint("javax.persistence.lock.timeout", String.valueOf(getLockTimeOutInMsForQueryGetEntity()));
        }

        return query;
    }
}
