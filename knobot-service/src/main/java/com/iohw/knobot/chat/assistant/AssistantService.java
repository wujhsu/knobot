package com.iohw.knobot.chat.assistant;

import com.iohw.knobot.chat.assistant.IAssistant.RAGAssistant;
import com.iohw.knobot.chat.assistant.IAssistant.base.StreamAssistant;
import com.iohw.knobot.chat.config.PersistentChatMemoryStore;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author: iohw
 * @date: 2025/4/16 21:37
 * @description:
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class AssistantService {
    private final ChatLanguageModel chatLanguageModel;

    private final StreamingChatLanguageModel streamingChatLanguageModel;

    private final PersistentChatMemoryStore chatMemoryStore;

    private final EmbeddingStoreContentRetriever embeddingStoreContentRetriever;

    @Bean
    public StreamAssistant streamAssistant() {
        return AiServices.builder(StreamAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .chatMemoryStore(chatMemoryStore)
                        .build())
                .build();
    }

    @Bean
    public RAGAssistant ragAssistant() {
        return AiServices.builder(RAGAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .contentRetriever(embeddingStoreContentRetriever)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .chatMemoryStore(chatMemoryStore)
                        .build())
                .build();
    }
}
