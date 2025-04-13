package com.iohw.knobot.chat.service;


import com.iohw.knobot.chat.dto.ChatSessionDto;
import com.iohw.knobot.response.Result;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/13 17:24
 * @description:
 */
public interface SessionSideBarService {
    Result<List<ChatSessionDto>> queryChatSessions(long userId);

    Result<String> createChatSession(long userId);
}
