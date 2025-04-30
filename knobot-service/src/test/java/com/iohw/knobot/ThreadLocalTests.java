package com.iohw.knobot;

import com.iohw.knobot.utils.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/30 22:38
 * @description:
 */
@Slf4j
public class ThreadLocalTests {
    @Test
    public void testThreadLocal() {
        ThreadLocalUtils.set("a", "aa");
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                log.info("a = {}", ThreadLocalUtils.get("a"));
            }, "thread" + i));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        log.info("a = {}", ThreadLocalUtils.get("a"));

    }
}
