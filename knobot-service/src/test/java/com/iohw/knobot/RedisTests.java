package com.iohw.knobot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author: iohw
 * @date: 2025/5/4 21:55
 * @description:
 */
@SpringBootTest(classes = KnobotServiceApplication.class)
public class RedisTests {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        stringRedisTemplate.opsForValue().set("key", "value");
    }
}
