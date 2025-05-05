package com.iohw.knobot.library.model.convert;

import com.iohw.knobot.library.model.KnowledgeLibDocumentDO;
import com.iohw.knobot.library.model.vo.KnowledgeLibDocumentVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/25 23:01
 * @description:
 */
@Mapper
public interface KnowledgeLibDocumentConvert {
    KnowledgeLibDocumentConvert INSTANCE = Mappers.getMapper(KnowledgeLibDocumentConvert.class);

    List<KnowledgeLibDocumentVO> toVO(List<KnowledgeLibDocumentDO> list);
}
