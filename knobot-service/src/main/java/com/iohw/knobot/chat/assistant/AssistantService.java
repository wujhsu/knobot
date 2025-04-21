package com.iohw.knobot.chat.assistant;

import com.iohw.knobot.chat.assistant.IAssistant.RAGAssistant;
import com.iohw.knobot.chat.config.PersistentChatMemoryStore;
import com.iohw.knobot.chat.config.ContentRetrieverFactory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private final ContentRetrieverFactory contentRetrieverFactory;

    // 缓存已创建的 RAG 助手实例
    private final Map<String, RAGAssistant> ragAssistantCache = new ConcurrentHashMap<>();

    public RAGAssistant getRagAssistant(String memoryId) {
        return ragAssistantCache.computeIfAbsent(memoryId, this::createRagAssistant);
    }

    private RAGAssistant createRagAssistant(String memoryId) {
        var retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetrieverFactory.createRetriever(memoryId))
                .contentInjector(DefaultContentInjector.builder()
                        .promptTemplate(PromptTemplate.from("{{userMessage}}\n补充信息如下:\n{{contents}}"))
                        .build())
                .build();

        return AiServices.builder(RAGAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemoryProvider(id -> MessageWindowChatMemory.builder()
                        .id(id)
                        .maxMessages(20)
                        .chatMemoryStore(chatMemoryStore)
                        .build())
                .build();
    }
}
