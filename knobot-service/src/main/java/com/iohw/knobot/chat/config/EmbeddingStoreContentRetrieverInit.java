package com.iohw.knobot.chat.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: iohw
 * @date: 2025/4/16 22:39
 * @description:
 */
@Configuration
@RequiredArgsConstructor
public class EmbeddingStoreContentRetrieverInit {
    final EmbeddingModel embeddingModel;

    final EmbeddingStore<TextSegment> embeddingStore;

    @Bean
    public EmbeddingStoreContentRetriever embeddingStoreContentRetriever() {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(5)
                .minScore(0.8)
                .build();
    }
}
