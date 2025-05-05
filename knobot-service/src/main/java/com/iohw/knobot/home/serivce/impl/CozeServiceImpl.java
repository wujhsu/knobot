package com.iohw.knobot.home.serivce.impl;

import com.alibaba.fastjson.JSONObject;
import com.iohw.knobot.common.request.CozeWorkFlowRequest;
import com.iohw.knobot.common.response.CozeWorkFlowResponse;
import com.iohw.knobot.coze.model.WeatherData;
import com.iohw.knobot.coze.request.DayWhetherRequest;
import com.iohw.knobot.home.serivce.CozeService;
import com.iohw.knobot.utils.CozeClient;
import org.springframework.stereotype.Service;

/**
 * @author: iohw
 * @date: 2025/4/29 23:22
 * @description:
 */
@Service
public class CozeServiceImpl implements CozeService {
    @Override
    public WeatherData getWeatherData(DayWhetherRequest request) {
        CozeWorkFlowRequest<DayWhetherRequest> workFlowRequest = new CozeWorkFlowRequest<>();
        workFlowRequest.setWorkflow_id("7498737967733751858");
        workFlowRequest.setParameters(request);

        CozeWorkFlowResponse workFlowResponse = CozeClient.reqWorkFlow(workFlowRequest);
        JSONObject jsonObject = JSONObject.parseObject(workFlowResponse.getData());
        WeatherData data = JSONObject.parseObject(jsonObject.getString("data"), WeatherData.class);

        return data;
    }
}
