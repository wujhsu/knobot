package com.iohw.knobot.chat.model.convert;

import com.iohw.knobot.chat.model.ChatConversationDO;
import com.iohw.knobot.chat.model.dto.ChatSessionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/13 17:34
 * @description:
 */
@Mapper
public interface ChatConversationConverter {
    ChatConversationConverter INSTANCE = Mappers.getMapper(ChatConversationConverter.class);

    ChatSessionDto toDto(ChatConversationDO chatConversationDO);

    List<ChatSessionDto> toDtoList(List<ChatConversationDO> chatConversationDO);
}
