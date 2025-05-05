package com.iohw.knobot.libary.service;

import com.iohw.knobot.library.model.KnowledgeLibDocumentDO;
import com.iohw.knobot.library.model.vo.KnowledgeLibDocumentVO;
import com.iohw.knobot.library.request.CreateKnowledgeLibDocCommand;
import com.iohw.knobot.library.request.DeleteKnowledgeLibDocCommand;
import com.iohw.knobot.library.request.UpdateKnowledgeLibDocCommand;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/25 21:46
 * @description: 知识库文档服务接口
 */
public interface KnowledgeLibDocumentService {
    /**
     * 添加文档
     */
    void addDocument(CreateKnowledgeLibDocCommand command);

    /**
     * 批量添加文档
     */
    void batchAddDocuments(List<KnowledgeLibDocumentDO> documents);

    /**
     * 根据知识库ID和文档ID获取文档
     */
    KnowledgeLibDocumentDO queryDocument(String knowledgeLibId, String documentId);

    /**
     * 获取知识库下的所有文档
     */
    List<KnowledgeLibDocumentVO> queryDocumentList(String knowledgeLibId);

    /**
     * 更新文档信息
     */
    void updateDocument(UpdateKnowledgeLibDocCommand command);

    /**
     * 更新文档状态
     */
    void updateDocumentStatus(String knowledgeLibId, String documentId, Integer status);

    /**
     * 删除文档
     */
    void deleteDocument(DeleteKnowledgeLibDocCommand command);

    /**
     * 批量删除文档
     */
    void batchDeleteDocuments(String knowledgeLibId, List<String> documentIds);

    /**
     * 获取知识库文档数量
     */
    int queryDocumentCount(String knowledgeLibId);
}