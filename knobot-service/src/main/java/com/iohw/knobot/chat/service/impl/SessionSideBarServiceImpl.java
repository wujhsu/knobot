package com.iohw.knobot.chat.service.impl;

import com.iohw.knobot.chat.convert.ChatSessionConverter;
import com.iohw.knobot.chat.entity.enums.ChatSessionEnum;
import com.iohw.knobot.chat.mapper.ChatSessionMapper;
import com.iohw.knobot.chat.request.command.CreateConversationCommand;
import com.iohw.knobot.chat.request.command.DeleteConversationCommand;
import com.iohw.knobot.chat.request.command.UpdateConversationTitleCommand;
import com.iohw.knobot.chat.service.SessionSideBarService;
import com.iohw.knobot.chat.vo.ChatSessionVO;
import com.iohw.knobot.response.Result;
import com.iohw.knobot.utils.IdGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iohw.knobot.chat.entity.dto.ChatSessionDto;
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
    public Result<List<ChatSessionDto>> queryChatConversation(long userId) {
        List<ChatSessionDO> chatSessionDOS = chatSessionMapper.selectByUserId(userId, 0);
        return Result.success(ChatSessionConverter.INSTANCE.toDtoList(chatSessionDOS));
    }

    @Override
    public Result<ChatSessionVO> createChatConversation(CreateConversationCommand request) {
        String memoryId = IdGeneratorUtil.generateId();
        Long userId = request.getUserId();
        ChatSessionDO chatSessionDO = ChatSessionDO.builder()
                .title(NEW_SESSION_TITLE)
                .status(ChatSessionEnum.NORMAL.getStatus())
                .memoryId(memoryId)
                .userId(userId)
                .build();
        chatSessionMapper.insert(chatSessionDO);
        return Result.success(ChatSessionVO.builder()
                .title(NEW_SESSION_TITLE)
                .memoryId(memoryId)
                .build());
    }

    @Override
    public Result<Void> deleteChatConversation(DeleteConversationCommand request) {
        chatSessionMapper.updateStatus(request.getMemoryId(), 2);
        return Result.success(null);
    }

    @Override
    public Result<Void> deleteChatConversationTitleUpdate(UpdateConversationTitleCommand command) {
        chatSessionMapper.updateTitle(command.getMemoryId(), command.getNewTitle());
        return Result.success(null);
    }
}
