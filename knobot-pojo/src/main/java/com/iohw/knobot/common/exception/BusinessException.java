package com.iohw.knobot.common.exception;

/**
 * @author: iohw
 * @date: 2025/5/4 22:50
 * @description:
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
