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
        String str = "123 \n解析理解下面的信息，回答用户的问题：456";
        System.out.println(str.substring(0, str.indexOf("\n解析理解下面的信息，回答用户的问题") == -1 ? str.length() : str.indexOf("\n解析理解下面的信息，回答用户的问题")));
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

    @Test
    public void test3() {
        System.out.println(IdGeneratorUtil.generateLibId());
    }

    @Test
    public void test4() {
        System.out.println(System.getenv());
    }
}
