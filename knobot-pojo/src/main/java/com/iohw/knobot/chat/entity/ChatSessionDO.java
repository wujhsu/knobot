package com.iohw.knobot.chat.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatSessionDO {
    private Long id;
    private String memoryId;
    private Long userId;
    private String title;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}