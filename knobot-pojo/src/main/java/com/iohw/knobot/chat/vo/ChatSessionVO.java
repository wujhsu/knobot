package com.iohw.knobot.chat.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author: iohw
 * @date: 2025/4/14 21:58
 * @description:
 */
@Data
@Builder
public class ChatSessionVO {
    private String memoryId;
    private String title;
}
