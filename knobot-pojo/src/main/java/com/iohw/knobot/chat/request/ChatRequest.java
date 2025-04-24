package com.iohw.knobot.chat.request;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2025/4/17 21:25
 * @description:
 */
@Data
public class ChatRequest {
    private String userId;
    private String userMessage;
    private String fileId;
    private Boolean isWebSearchRequest;
}
