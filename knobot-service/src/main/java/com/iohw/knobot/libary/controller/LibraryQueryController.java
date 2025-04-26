package com.iohw.knobot.libary.controller;

import com.iohw.knobot.libary.service.KnowledgeLibDocumentService;
import com.iohw.knobot.libary.service.KnowledgeLibService;
import com.iohw.knobot.library.entity.vo.KnowledgeLibDocumentVO;
import com.iohw.knobot.library.entity.vo.KnowledgeLibNameVO;
import com.iohw.knobot.library.entity.vo.KnowledgeLibVO;
import com.iohw.knobot.library.request.QueryDocumentLibRequest;
import com.iohw.knobot.library.request.QueryLibraryDetailListRequest;
import com.iohw.knobot.library.request.QueryLibraryListRequest;
import com.iohw.knobot.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/25 22:29
 * @description:
 */
@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryQueryController {
    final KnowledgeLibService knowledgeLibService;
    final KnowledgeLibDocumentService knowledgeLibDocumentService;

    @GetMapping("/queryLibraryDetailList")
    public Result<List<KnowledgeLibVO>> queryLibraryDetailList(QueryLibraryDetailListRequest request) {
        return Result.success(knowledgeLibService.queryLibraryDetailList(request));
    }

    @GetMapping("/queryLibraryList")
    public Result<List<KnowledgeLibNameVO>> queryLibraryList(QueryLibraryListRequest request) {
        return Result.success(knowledgeLibService.queryKnowledgeLibList(request));
    }

    @GetMapping("queryLibraryDocumentList")
    public Result<List<KnowledgeLibDocumentVO>> queryLibraryDocumentList(QueryDocumentLibRequest request) {
        return Result.success(knowledgeLibDocumentService.queryDocumentList(request.getKnowledgeLibId()));
    }
}
