package com.iohw.knobot.chat.service.impl;

import com.iohw.knobot.chat.entity.convert.ChatMessageConverter;
import com.iohw.knobot.chat.entity.ChatMessageDO;
import com.iohw.knobot.chat.entity.dto.ChatMessageDto;
import com.iohw.knobot.chat.mapper.ChatMessageMapper;
import com.iohw.knobot.chat.service.ChatService;
import com.iohw.knobot.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/13 18:16
 * @description:
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public Result<List<ChatMessageDto>> queryHistoryMessages(String memoryId) {
        List<ChatMessageDO> chatMessageDOS = chatMessageMapper.selectByMemoryId(memoryId);
        return Result.success(ChatMessageConverter.INSTANCE.toDtoList(chatMessageDOS));
    }
}
