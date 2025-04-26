package com.iohw.knobot.library.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: iohw
 * @date: 2025/4/25 22:39
 * @description:
 */
@Data
@Builder
public class KnowledgeLibVO {
    private String knowledgeLibId;
    private String knowledgeLibName;
    private String knowledgeLibDesc;
    private Integer documentCount;
    private LocalDateTime createTime;
}
