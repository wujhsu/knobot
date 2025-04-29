package com.iohw.knobot.home.serivce;

import com.iohw.knobot.coze.entity.WeatherData;
import com.iohw.knobot.coze.request.DayWhetherRequest;

/**
 * @author: iohw
 * @date: 2025/4/29 23:22
 * @description:
 */
public interface CozeService {
    WeatherData getWeatherData(DayWhetherRequest request);
}
