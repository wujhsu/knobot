package com.iohw.knobot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.iohw.knobot.common.request.CozeWorkFlowRequest;
import com.iohw.knobot.common.response.CozeWorkFlowResponse;
import com.iohw.knobot.coze.entity.WeatherData;
import com.iohw.knobot.coze.request.DayWhetherRequest;
import com.iohw.knobot.coze.response.DayWhetherResponse;
import com.iohw.knobot.utils.CozeClient;
import lombok.Builder;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author: iohw
 * @date: 2025/4/28 22:39
 * @description:
 */
public class CozeHttpTests {
    static class CozeRequest {
        public String workflow_id;
        public Parameters parameters;
        static class Parameters {
            public String content;
            public String lang;
        }
    }
    @Test
    public void test() throws InterruptedException {
        OkHttpClient client = new OkHttpClient();
        CozeRequest cozeRequest = new CozeRequest();
        CozeRequest.Parameters parameters = new CozeRequest.Parameters();
        parameters.lang = "英文";
        parameters.content = "你今天过得怎么样";
        cozeRequest.workflow_id = "7498366386819301413";
        cozeRequest.parameters = parameters;
        String json = JSON.toJSONString(cozeRequest);


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url("https://api.coze.cn/v1/workflow/run")
                .post(body)
                .addHeader("Authorization", "Bearer pat_h1zJDVSnghxBxOcS3KlYgX8kdzOqGCjZCt5CMMSLBJWX3XXCeawkeeQgSKgsheZk")
                .addHeader("Content-Type", "application/json")
                .build();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.err.println("请求失败: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseData = response.body().string();
                    countDownLatch.countDown();
                    JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(responseData);
                    System.out.println("异步响应: " + responseData);
                } finally {
                    response.close();
                }
            }
        });
        countDownLatch.await();
    }
    static class Translation {
        String content;
        String lang;
    }
    @Test
    public void test1() {
        Translation translation = new Translation();
        translation.content = "今天天气如何";
        translation.lang = "英文";
        CozeWorkFlowRequest<Translation> workFlowRequest = new CozeWorkFlowRequest<>();
        workFlowRequest.setWorkflow_id("7498366386819301413");
        workFlowRequest.setParameters(translation);
        CozeWorkFlowResponse cozeWorkFlowResponse = CozeClient.reqWorkFlow(workFlowRequest);
        System.out.println(cozeWorkFlowResponse);

    }

    @Test
    public void test2() {
        DayWhetherRequest dayWhetherRequest = new DayWhetherRequest();
        dayWhetherRequest.setCity("杭州");
        CozeWorkFlowRequest<DayWhetherRequest> workFlowRequest = new CozeWorkFlowRequest<>();
        workFlowRequest.setWorkflow_id("7498737967733751858");
        workFlowRequest.setParameters(dayWhetherRequest);

        CozeWorkFlowResponse cozeWorkFlowResponse = CozeClient.reqWorkFlow(workFlowRequest);

        JSONObject jsonObject = JSONObject.parseObject(cozeWorkFlowResponse.getData());
        WeatherData data = JSONObject.parseObject(jsonObject.getString("data"), WeatherData.class);

        System.out.println(data);

    }
}
