package com.iohw.knobot.chat.service;

import com.iohw.knobot.chat.dto.ChatMessageDto;
import com.iohw.knobot.response.Result;

import java.util.List;

/**
 * @author: iohw
 * @date: 2025/4/13 18:16
 * @description:
 */
public interface ChatService {
    Result<List<ChatMessageDto>> queryHistoryMessages(String memoryId);
}
