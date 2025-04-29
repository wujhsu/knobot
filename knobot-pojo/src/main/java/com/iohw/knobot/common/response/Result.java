package com.iohw.knobot.common.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author: iohw
 * @date: 2025/4/13 23:13
 * @description:
 */
@Data
@Builder
public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder().code("200").data(data).build();
    }
}
