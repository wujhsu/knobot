package com.iohw.knobot.chat.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: iohw
 * @date: 2025/4/16 22:24
 * @description:
 */
@Configuration
@RequiredArgsConstructor
public class PgVectorEmbeddingStoreInit {
    final PgConfig pgConfig;

    @Bean
    EmbeddingStore<TextSegment> initEmbeddingStore() {
        return PgVectorEmbeddingStore.builder()
                .host(pgConfig.getHost())
                .port(pgConfig.getPort())
                .user(pgConfig.getUser())
                .password(pgConfig.getPassword())
                .database(pgConfig.getDatabase())
                .table(pgConfig.getTable())
                .dimension(1024)
                .dropTableFirst(false)
                .createTable(true)
                .build();

    }
}
