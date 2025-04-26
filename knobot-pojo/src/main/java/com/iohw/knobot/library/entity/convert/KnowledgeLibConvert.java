package com.iohw.knobot.library.entity.convert;

import com.iohw.knobot.library.entity.KnowledgeLibDO;
import com.iohw.knobot.library.entity.vo.KnowledgeLibVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/25 23:01
 * @description:
 */
@Mapper
public interface KnowledgeLibConvert {
    KnowledgeLibConvert INSTANCE = Mappers.getMapper(KnowledgeLibConvert.class);

    List<KnowledgeLibVO> toVOList(List<KnowledgeLibDO> list);
}
