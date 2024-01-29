package com.nix.ecommerceapi;

import com.nix.ecommerceapi.service.InventoryService;
import com.nix.ecommerceapi.service.locker.RedisLocker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class LockerTest {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private RedisLocker redisLocker;

    @Test
    public void test() {
        assertTrue(true);
        for (int i = 0; i < 10; ++i) {
            CompletableFuture.runAsync(() -> runTask("task"));
        }
    }

    private void runTask(String taskNumber) {
        log.info("Running task: {}", taskNumber);
        inventoryService.processOrderAndSaveInventory(1L, 1L);
        log.info("Task {} finished with result: ", taskNumber);
    }
}
