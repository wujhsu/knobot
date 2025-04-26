package com.iohw.knobot;

import com.iohw.knobot.chat.assistant.AssistantService;
import com.iohw.knobot.chat.assistant.IAssistant.WebSearchAssistant;
import com.iohw.knobot.chat.assistant.IAssistant.base.Assistant;
import com.iohw.knobot.config.properties.WebSearchProperties;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.searchapi.SearchApiWebSearchEngine;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: iohw
 * @date: 2025/4/23 21:20
 * @description:
 */
@SpringBootTest(classes = KnobotServiceApplication.class)
public class WebSearchTests {
    @Autowired
    private ChatLanguageModel chatLanguageModel;

    @Autowired
    private WebSearchAssistant assistant;
    @Test
    public void test2() {
        TokenStream chat = assistant.chat("1", "杭州天气如何");
        chat.onPartialResponse(item -> {
            System.out.println(item);
        });
    }
    @Test
    public void test() {
        WebSearchEngine webSearchEngine = SearchApiWebSearchEngine.builder()
                .apiKey("a3NXKiXu2AiRg68BJBiegThg")
                .engine("google")
                .build();
        WebSearchContentRetriever searchContentRetriever = WebSearchContentRetriever.builder()
                .webSearchEngine(webSearchEngine)
                .maxResults(3)
                .build();
        QueryRouter queryRouter = new DefaultQueryRouter(searchContentRetriever);

        DefaultRetrievalAugmentor augmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .retrievalAugmentor(augmentor)
                .build();

        String chat = assistant.chat("杭州今天天气是什么");
        System.out.println(chat);
    }
}
