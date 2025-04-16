package com.iohw.knobot.chat.service.impl;

import com.iohw.knobot.chat.convert.ChatSessionConverter;
import com.iohw.knobot.chat.enums.ChatSessionEnum;
import com.iohw.knobot.chat.mapper.ChatSessionMapper;
import com.iohw.knobot.chat.request.CreateSessionRequest;
import com.iohw.knobot.chat.service.SessionSideBarService;
import com.iohw.knobot.chat.vo.ChatSessionVO;
import com.iohw.knobot.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iohw.knobot.chat.dto.ChatSessionDto;
import com.iohw.knobot.chat.entity.ChatSessionDO;
import java.util.List;
import java.util.UUID;

import static com.iohw.knobot.chat.constant.ChatConstant.NEW_SESSION_TITLE;

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
    public Result<ChatSessionVO> createChatSession(CreateSessionRequest request) {
        String uuid = UUID.randomUUID().toString();
        Long userId = request.getUserId();
        ChatSessionDO chatSessionDO = ChatSessionDO.builder()
                .title(NEW_SESSION_TITLE)
                .status(ChatSessionEnum.NORMAL.getStatus())
                .memoryId(uuid)
                .userId(userId)
                .build();
        chatSessionMapper.insert(chatSessionDO);
        return Result.success(ChatSessionVO.builder()
                .title(NEW_SESSION_TITLE)
                .memoryId(uuid)
                .build());
    }
}
