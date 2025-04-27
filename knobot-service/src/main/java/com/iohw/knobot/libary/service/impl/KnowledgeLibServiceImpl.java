package com.iohw.knobot.libary.service.impl;

import com.iohw.knobot.libary.mapper.KnowledgeLibDocumentMapper;
import com.iohw.knobot.library.entity.KnowledgeLibDO;
import com.iohw.knobot.libary.mapper.KnowledgeLibMapper;
import com.iohw.knobot.libary.service.KnowledgeLibService;
import com.iohw.knobot.library.entity.convert.KnowledgeLibConvert;
import com.iohw.knobot.library.entity.vo.KnowledgeLibNameVO;
import com.iohw.knobot.library.entity.vo.KnowledgeLibVO;
import com.iohw.knobot.library.request.*;
import com.iohw.knobot.utils.IdGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: iohw
 * @date: 2025/4/25 21:46
 * @description: 知识库服务实现类
 */
@Service
@RequiredArgsConstructor
public class KnowledgeLibServiceImpl implements KnowledgeLibService {
    private final KnowledgeLibDocumentMapper documentMapper;
    private final KnowledgeLibMapper knowledgeLibMapper;

    @Override
    @Transactional
    public void createKnowledgeLib(CreateKnowledgeLibCommand command) {
        KnowledgeLibDO knowledgeLibDO = new KnowledgeLibDO();
        knowledgeLibDO.setUserId(command.getUserId());
        knowledgeLibDO.setKnowledgeLibId(IdGeneratorUtil.generateLibId());
        knowledgeLibDO.setKnowledgeLibName(command.getKnowledgeLibName());
        knowledgeLibDO.setKnowledgeLibDesc(command.getKnowledgeLibDesc());
        knowledgeLibDO.setDocumentCount(0);
        knowledgeLibMapper.insert(knowledgeLibDO);
    }

    @Override
    public KnowledgeLibDO getKnowledgeLib(String knowledgeLibId) {
        return knowledgeLibMapper.selectById(knowledgeLibId);
    }

    @Override
    public List<KnowledgeLibVO> queryLibraryDetailList(QueryLibraryDetailListRequest request) {
        List<KnowledgeLibDO> knowledgeLibDOS = knowledgeLibMapper.selectByUserId(request.getUserId());

        return KnowledgeLibConvert.INSTANCE.toVOList(knowledgeLibDOS);
    }

    @Override
    @Transactional
    public void updateKnowledgeLib(UpdateKnowledgeLibCommand command) {
        KnowledgeLibDO knowledgeLibDO = new KnowledgeLibDO();
        knowledgeLibDO.setKnowledgeLibId(command.getKnowledgeLibId());
        knowledgeLibDO.setKnowledgeLibName(command.getKnowledgeLibName());
        knowledgeLibDO.setKnowledgeLibDesc(command.getKnowledgeLibDesc());
        knowledgeLibMapper.update(knowledgeLibDO);
    }

    @Override
    @Transactional
    public void updateDocumentCount(String knowledgeLibId, Integer count) {
        knowledgeLibMapper.updateDocumentCount(knowledgeLibId, count);
    }

    @Override
    @Transactional
    public void deleteKnowledgeLib(DeleteKnowledgeLibCommand command) {
        // 1.删除知识库关联的文档
        documentMapper.deleteByKnowledgeLibId(command.getKnowledgeLibId());
        // 2.删除知识库
        knowledgeLibMapper.deleteById(command.getKnowledgeLibId());
    }

    @Override
    public List<KnowledgeLibNameVO> queryKnowledgeLibList(QueryLibraryListRequest request) {
        List<KnowledgeLibDO> list = knowledgeLibMapper.selectByUserId(request.getUserId());
        return list.stream()
                .map(item -> KnowledgeLibNameVO.builder()
                        .knowledgeLibName(item.getKnowledgeLibName())
                        .knowledgeLibId(item.getKnowledgeLibId())
                        .build())
                .toList();
    }
}