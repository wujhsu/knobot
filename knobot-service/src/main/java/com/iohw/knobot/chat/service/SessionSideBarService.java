package com.iohw.knobot.chat.service;


import com.iohw.knobot.chat.entity.dto.ChatSessionDto;
import com.iohw.knobot.chat.request.command.CreateConversationCommand;
import com.iohw.knobot.chat.request.command.DeleteConversationCommand;
import com.iohw.knobot.chat.request.command.UpdateConversationTitleCommand;
import com.iohw.knobot.chat.vo.ChatSessionVO;
import com.iohw.knobot.response.Result;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/13 17:24
 * @description:
 */
public interface SessionSideBarService {
    Result<List<ChatSessionDto>> queryChatSessions(long userId);

    Result<ChatSessionVO> createChatSession(CreateConversationCommand userId);

    Result<Void> deleteChatConversation(DeleteConversationCommand request);

    Result<Void> deleteChatConversationTitleUpdate(UpdateConversationTitleCommand command);
}
