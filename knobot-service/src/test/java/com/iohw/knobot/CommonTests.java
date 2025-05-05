package com.iohw.knobot;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.iohw.knobot.common.enums.ErrorEnum;
import com.iohw.knobot.utils.IdGeneratorUtil;
import com.iohw.knobot.utils.IpAddressUtil;
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

    @Test
    public void test5() throws NoApiKeyException, InputRequiredException {
        ApplicationParam applicationParam = ApplicationParam.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .appId("61b457c80b644d9e9334114129045fbd")
                .prompt("杭州今天天气如何")
                .build();
        Application application = new Application();
        ApplicationResult call = application.call(applicationParam);
        System.out.println("call.getOutput().getText() = " + call.getOutput().getText());

    }

    @Test
    public void test6() {
        System.out.println(IpAddressUtil.getLocalIP());
    }

    @Test
    public void test7() {
        System.out.println(ErrorEnum.USER_NOT_EXIST.getDesc());
    }
}
