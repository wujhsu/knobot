package com.iohw.knobot.chat.assistant.IAssistant.base;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

/**
 * @author: iohw
 * @date: 2025/4/12 20:54
 * @description:
 */
public interface StreamAssistant {
    TokenStream chat(@MemoryId String memoryId, @UserMessage String input);
}
