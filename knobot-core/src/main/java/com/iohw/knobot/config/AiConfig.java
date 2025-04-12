package com.iohw.knobot.config;

import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: iohw
 * @date: 2025/4/12 20:12
 * @description:
 */
@Configuration
public class AiConfig {
    private final LangChainProperties properties;

    public AiConfig(LangChainProperties properties) {
        this.properties = properties;
    }

    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(properties.getApiKey())
                .modelName(properties.getModelName())
                .baseUrl(properties.getBaseUrl())
                .build();
    }
}
