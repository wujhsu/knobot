package com.iohw.knobot.library.model.vo;

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
public class KnowledgeLibDocumentVO {
    private String documentId;
    private String documentName;
    private String documentDesc;
    private Double documentSize;
    private int status;
    private LocalDateTime uploadTime;
}
