package com.iohw.knobot;

import com.iohw.knobot.utils.IdGeneratorUtil;
import org.junit.jupiter.api.Test;

/**
 * @author: iohw
 * @date: 2025/4/18 21:49
 * @description:
 */
public class CommonTests {
    @Test
    public void test() {
        String str = "123 \n基于下面的信息回答：456";
        System.out.println(str.substring(0, str.indexOf("\n基于下面的信息回答") == -1 ? str.length() : str.indexOf("\n基于下面的信息回答")));
    }

    @Test
    public void test2() {

        // 测试高并发下的ID生成
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ": " + IdGeneratorUtil.generateId());
            }).start();
        }
    }
}
