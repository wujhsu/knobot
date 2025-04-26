package com.iohw.knobot.chat.entity.convert;

import com.iohw.knobot.chat.entity.ChatMessageDO;
import com.iohw.knobot.chat.entity.dto.ChatMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/13 18:17
 * @description:
 */
@Mapper
public interface ChatMessageConverter {
    ChatMessageConverter INSTANCE = Mappers.getMapper(ChatMessageConverter.class);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "content", source = "content")
    ChatMessageDto toDto(ChatMessageDO chatMessageDO);

    List<ChatMessageDto> toDtoList(List<ChatMessageDO> chatMessageDOS);
}
