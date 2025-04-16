package com.iohw.knobot.chat.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author: iohw
 * @date: 2025/4/13 17:26
 * @description:
 */
@Data
@Builder
public class ChatSessionDto {
    private String memoryId;
    private String userId;
    private String title;
}
