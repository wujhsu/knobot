package com.iohw.knobot.libary.service.impl;

import com.iohw.knobot.common.dto.FileUploadDto;
import com.iohw.knobot.library.entity.KnowledgeLibDocumentDO;
import com.iohw.knobot.libary.mapper.KnowledgeLibDocumentMapper;
import com.iohw.knobot.libary.service.KnowledgeLibDocumentService;
import com.iohw.knobot.libary.service.KnowledgeLibService;
import com.iohw.knobot.library.entity.convert.KnowledgeLibDocumentConvert;
import com.iohw.knobot.library.entity.vo.KnowledgeLibDocumentVO;
import com.iohw.knobot.library.request.CreateKnowledgeLibDocCommand;
import com.iohw.knobot.library.request.UpdateKnowledgeLibDocCommand;
import com.iohw.knobot.upload.UploadFileStrategy;
import com.iohw.knobot.utils.FileUtils;
import com.iohw.knobot.utils.IdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/25 21:46
 * @description: 知识库文档服务实现类
 */
@Service
@RequiredArgsConstructor
public class KnowledgeLibDocumentServiceImpl implements KnowledgeLibDocumentService {
    private final KnowledgeLibDocumentMapper documentMapper;
    private final KnowledgeLibService knowledgeLibService;
    private final UploadFileStrategy uploadFileStrategy;

    @Override
    @Transactional
    public void addDocument(CreateKnowledgeLibDocCommand command) {
        KnowledgeLibDocumentDO documentDO = new KnowledgeLibDocumentDO();
        documentDO.setDocumentName(command.getDocumentName());
        documentDO.setDocumentDesc(command.getDocumentDesc());
        documentDO.setDocumentId(IdGeneratorUtil.generateDocId());
        documentDO.setKnowledgeLibId(command.getKnowledgeLibId());
        FileUploadDto upload = uploadFileStrategy.upload(command.getFile(), "/documents");
        documentDO.setPath(upload.getFilePath());
        documentDO.setDocumentSize(FileUtils.getFileSizeInMB(command.getFile()));
        documentMapper.insert(documentDO);

        // 更新文档数量
        updateKnowledgeLibDocumentCount(documentDO.getKnowledgeLibId());
    }

    @Override
    @Transactional
    public void batchAddDocuments(List<KnowledgeLibDocumentDO> documents) {
        if (!documents.isEmpty()) {
            documentMapper.batchInsert(documents);
            updateKnowledgeLibDocumentCount(documents.get(0).getKnowledgeLibId());
        }
    }

    @Override
    public KnowledgeLibDocumentDO queryDocument(String knowledgeLibId, String documentId) {
        return documentMapper.selectById(knowledgeLibId, documentId);
    }

    @Override
    public List<KnowledgeLibDocumentVO> queryDocumentList(String knowledgeLibId) {
        return KnowledgeLibDocumentConvert.INSTANCE.toVO(documentMapper.selectListByKnowledgeLibId(knowledgeLibId));
    }

    @Override
    @Transactional
    public void updateDocument(UpdateKnowledgeLibDocCommand command) {
        KnowledgeLibDocumentDO documentDO = new KnowledgeLibDocumentDO();
        documentDO.setDocumentName(command.getDocumentName());
        documentDO.setDocumentDesc(command.getDocumentDesc());
        documentDO.setDocumentId(command.getDocumentId());
        if(command.getFile() != null) {
            FileUploadDto upload = uploadFileStrategy.upload(command.getFile(), "/documents");
            documentDO.setPath(upload.getFilePath());
            documentDO.setDocumentSize(FileUtils.getFileSizeInMB(command.getFile()));
        }
        documentMapper.update(documentDO);
    }

    @Override
    @Transactional
    public void updateDocumentStatus(String knowledgeLibId, String documentId, Integer status) {
        documentMapper.updateStatus(knowledgeLibId, documentId, status);
    }

    @Override
    @Transactional
    public void deleteDocument(String knowledgeLibId, String documentId) {
        documentMapper.deleteById(knowledgeLibId, documentId);
        updateKnowledgeLibDocumentCount(knowledgeLibId);
    }

    @Override
    @Transactional
    public void batchDeleteDocuments(String knowledgeLibId, List<String> documentIds) {
        documentMapper.batchDelete(knowledgeLibId, documentIds);
        updateKnowledgeLibDocumentCount(knowledgeLibId);
    }

    @Override
    public int queryDocumentCount(String knowledgeLibId) {
        return documentMapper.selectCountByKnowledgeLibId(knowledgeLibId);
    }

    /**
     * 更新知识库的文档数量
     */
    private void updateKnowledgeLibDocumentCount(String knowledgeLibId) {
        int count = queryDocumentCount(knowledgeLibId);
        knowledgeLibService.updateDocumentCount(knowledgeLibId, count);
    }
}