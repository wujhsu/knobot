package com.iohw.knobot.library.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author: iohw
 * @date: 2025/4/26 17:14
 * @description:
 */
@Data
@Builder
public class KnowledgeLibNameVO {
    private String knowledgeLibId;
    private String knowledgeLibName;
}
