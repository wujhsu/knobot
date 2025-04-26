package com.iohw.knobot.chat.entity.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author: iohw
 * @date: 2025/4/13 18:16
 * @description:
 */
@Data
@Builder
public class ChatMessageDto {
    private String role;
    private String content;
}
