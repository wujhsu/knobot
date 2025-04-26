package com.iohw.knobot.chat.config;

import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: iohw
 * @date: 2025/4/18 23:27
 * @description:
 */
@Configuration
@RequiredArgsConstructor
public class RAGInit {
    final ContentRetrieverFactory contentRetrieverFactory;

    @Bean
    RetrievalAugmentor retrievalAugmentor() {
        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetrieverFactory.createRetriever(null, null))
                .contentInjector(DefaultContentInjector.builder()
                        .promptTemplate(PromptTemplate.from("{{userMessage}}\n补充信息如下:\n{{contents}}"))
                        .build())
                .build();
    }
}
