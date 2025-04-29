package com.iohw.knobot.coze.response;

import com.iohw.knobot.coze.entity.WeatherData;
import lombok.Data;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/29 22:55
 * @description:
 */
@Data
public class DayWhetherResponse {
    private Integer code;
    private List<WeatherData> data;
    private String message;

}
