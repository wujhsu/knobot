package com.iohw.knobot.chat.entity.convert;

import com.iohw.knobot.chat.entity.ChatSessionDO;
import com.iohw.knobot.chat.entity.dto.ChatSessionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/13 17:34
 * @description:
 */
@Mapper
public interface ChatSessionConverter {
    ChatSessionConverter INSTANCE = Mappers.getMapper(ChatSessionConverter.class);

    ChatSessionDto toDto(ChatSessionDO chatSessionDO);

    List<ChatSessionDto> toDtoList(List<ChatSessionDO> chatSessionDO);
}
