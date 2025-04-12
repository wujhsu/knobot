package com.iohw.knobot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "langchain4j.open-ai")
public class LangChainProperties {
    private String apiKey;
    private String modelName;
    private String baseUrl;
}