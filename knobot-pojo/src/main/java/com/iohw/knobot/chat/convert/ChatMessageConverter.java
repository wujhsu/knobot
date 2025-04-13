package com.iohw.knobot.chat.convert;

import com.iohw.knobot.chat.entity.ChatMessageDO;
import com.iohw.knobot.chat.dto.ChatMessageDto;
import dev.langchain4j.data.message.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/13 18:17
 * @description:
 */
@Mapper(componentModel = "spring")
public interface ChatMessageConverter {
    ChatMessageConverter INSTANCE = Mappers.getMapper(ChatMessageConverter.class);

    @Mapping(target = "role", expression = "java(chatMessage.type().toString().toLowerCase())")
    @Mapping(target = "content", expression = "java(chatMessage.text())")
    ChatMessageDto dtoToDto(ChatMessage chatMessage);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "content", source = "content")
    ChatMessageDto toDto(ChatMessageDO chatMessageDO);

    List<ChatMessageDto> toDtoList(List<ChatMessageDO> chatMessageDOS);
}
