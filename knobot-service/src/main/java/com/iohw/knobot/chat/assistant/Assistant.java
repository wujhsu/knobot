package com.iohw.knobot.chat.assistant;

import dev.langchain4j.service.TokenStream;

/**
 * @author: iohw
 * @date: 2025/4/12 20:54
 * @description:
 */
public interface Assistant {
    TokenStream chat(String input);
}
