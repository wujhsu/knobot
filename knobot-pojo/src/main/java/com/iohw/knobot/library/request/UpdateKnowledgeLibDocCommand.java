package com.iohw.knobot.library.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: iohw
 * @date: 2025/4/26 16:44
 * @description:
 */
@Data
public class UpdateKnowledgeLibDocCommand {
    private String documentId;
    private String documentName;
    private String documentDesc;
    private MultipartFile file;
}
