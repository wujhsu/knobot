package com.iohw.knobot;

import com.iohw.knobot.chat.assistant.IAssistant.RAGAssistant;
import com.iohw.knobot.chat.assistant.IAssistant.base.Assistant;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static dev.langchain4j.data.document.Metadata.metadata;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

/**
 * @author: iohw
 * @date: 2025/4/21 21:53
 * @description:
 */
@SpringBootTest(classes = KnobotServiceApplication.class)
public class FilterTests {
    @Autowired
    private ChatLanguageModel chatLanguageModel;
    @Autowired
    private EmbeddingModel embeddingModel;


    @Test
    void Static_Metadata_Filter_Example() {
        // given
        TextSegment dogsSegment = TextSegment.from("关于狗的文章", metadata("animal", "dog"));
        TextSegment birdsSegment = TextSegment.from("关于鸟的文章", metadata("animal", "7fb5f60b-bf66-4719-be9d-0c3619d9a0e1"));

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.add(embeddingModel.embed(dogsSegment).content(), dogsSegment);
        embeddingStore.add(embeddingModel.embed(birdsSegment).content(), birdsSegment);
        // embeddingStore contains segments about both dogs and birds

        Filter onlyDogs = metadataKey("animal").isEqualTo("7fb5f60b-bf66-4719-be9d-0c3619d9a0e1");

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .filter(onlyDogs) // by specifying the static filter, we limit the search to segments only about dogs
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(contentRetriever)
                .build();

        // when
        String answer = assistant.chat("这篇文章是关于什么的？");

        System.out.println(answer);
    }
}
