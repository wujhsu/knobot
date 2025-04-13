package com.iohw.knobot.chat.config;

import com.iohw.knobot.chat.assistant.Assistant;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: iohw
 * @date: 2025/4/13 18:46
 * @description:
 */
@Configuration
public class Langchain4jConfiguration {
    @Autowired
    private StreamingChatLanguageModel model;
    @Autowired
    private PersistentChatMemoryStore chatMemoryStore;

    @Bean("basicAssistant")
    public Assistant baicAssistant() {
        return AiServices.builder(Assistant.class)
                .streamingChatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .chatMemoryStore(chatMemoryStore)
                        .build())
                .build();
    }
}
