package com.iohw.knobot.library.request;

import lombok.Data;

/**
 * @author: iohw
 * @date: 2025/4/25 23:48
 * @description:
 */
@Data
public class CreateKnowledgeLibCommand {
    private String userId;
    private String knowledgeLibName;
    private String knowledgeLibDesc;
}
