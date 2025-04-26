package com.iohw.knobot.libary.controller;

import com.iohw.knobot.libary.service.KnowledgeLibDocumentService;
import com.iohw.knobot.libary.service.KnowledgeLibService;
import com.iohw.knobot.library.request.CreateKnowledgeLibCommand;
import com.iohw.knobot.library.request.CreateKnowledgeLibDocCommand;
import com.iohw.knobot.library.request.UpdateKnowledgeLibCommand;
import com.iohw.knobot.library.request.UpdateKnowledgeLibDocCommand;
import com.iohw.knobot.response.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

/**
 * @author: iohw
 * @date: 2025/4/24 21:25
 * @description:
 */
@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryController {
    final KnowledgeLibService knowledgeLibService;
    final KnowledgeLibDocumentService knowledgeLibDocumentService;

    @PostMapping("/createKnowledgeLib")
    public Result<Void> createKnowledgeLib(@RequestBody CreateKnowledgeLibCommand command) {
        knowledgeLibService.createKnowledgeLib(command);
        return Result.success(null);
    }

    @PostMapping("/updateKnowledgeLib")
    public Result<Void> updateKnowledgeLib(@RequestBody UpdateKnowledgeLibCommand command) {
        knowledgeLibService.updateKnowledgeLib(command);
        return Result.success(null);
    }

    @PostMapping("/createKnowledgeLibDocument")
    public Result<Void> createKnowledgeLibDocument(CreateKnowledgeLibDocCommand command) {
        knowledgeLibDocumentService.addDocument(command);
        return Result.success(null);
    }

    @PostMapping("/updateKnowledgeLibDocument")
    public Result<Void> updateKnowledgeLibDocument(UpdateKnowledgeLibDocCommand command) {
        knowledgeLibDocumentService.updateDocument(command);
        return Result.success(null);
    }
}
