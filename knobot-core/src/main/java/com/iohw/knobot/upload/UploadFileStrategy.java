package com.iohw.knobot.upload;

import com.iohw.knobot.common.dto.FileUploadDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: iohw
 * @date: 2025/4/26 10:03
 * @description:
 */
public interface UploadFileStrategy {
    FileUploadDto upload(MultipartFile file, String path);
}
