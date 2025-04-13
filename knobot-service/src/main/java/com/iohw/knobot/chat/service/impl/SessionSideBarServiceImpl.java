package com.iohw.knobot.chat.service.impl;

import com.iohw.knobot.chat.convert.ChatSessionConverter;
import com.iohw.knobot.chat.mapper.ChatSessionMapper;
import com.iohw.knobot.chat.service.SessionSideBarService;
import com.iohw.knobot.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iohw.knobot.chat.dto.ChatSessionDto;
import com.iohw.knobot.chat.entity.ChatSessionDO;
import java.util.List;
import java.util.UUID;

/**
 * @author: iohw
 * @date: 2025/4/13 17:27
 * @description:
 */
@Service
public class SessionSideBarServiceImpl implements SessionSideBarService {
    @Autowired
    private ChatSessionMapper chatSessionMapper;

    @Override
    public Result<List<ChatSessionDto>> queryChatSessions(long userId) {
        List<ChatSessionDO> chatSessionDOS = chatSessionMapper.selectByUserId(userId, 0);
        return Result.success(ChatSessionConverter.INSTANCE.toDtoList(chatSessionDOS));
    }

    @Override
    public Result<String> createChatSession(long userId) {
        String uuid = UUID.randomUUID().toString();
        ChatSessionDO.builder()
                .title("新对话")
                .status(0)
                .memoryId(uuid)
                .userId(userId)
                .build();
        return Result.success(uuid);
    }
}
