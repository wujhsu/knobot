package com.iohw.knobot.chat.controller;

import com.iohw.knobot.chat.assistant.IAssistant.RAGAssistant;
import com.iohw.knobot.chat.assistant.IAssistant.base.Assistant;
import com.iohw.knobot.chat.assistant.IAssistant.base.StreamAssistant;
import com.iohw.knobot.chat.dto.ChatSessionDto;
import com.iohw.knobot.chat.dto.ChatMessageDto;
import com.iohw.knobot.chat.request.CreateSessionRequest;
import com.iohw.knobot.chat.service.ChatService;
import com.iohw.knobot.chat.service.SessionSideBarService;
import com.iohw.knobot.chat.vo.ChatSessionVO;
import com.iohw.knobot.response.Result;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

/**
 * @author: iohw
 * @date: 2025/4/12 19:37
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    final SessionSideBarService sessionSideBarService;

    final ChatService chatService;

    final ChatLanguageModel chatLanguageModel;

    final EmbeddingModel embeddingModel;

    final EmbeddingStore<TextSegment> embeddingStore;

    final RAGAssistant assistant;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam("memoryId") String memoryId,  @RequestParam("input") String input) {
        SseEmitter emitter = new SseEmitter(-1L); // 无超时
        try {
            TokenStream tokenStream = assistant.chat(memoryId, input);
            tokenStream
                    .onPartialResponse(token -> {
                        try {
                            emitter.send(SseEmitter.event()
                                    .data(token)
                                    .id(String.valueOf(System.currentTimeMillis()))
                                    .name("message"));
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                        }
                    })
                    .onCompleteResponse(response -> {
                        try {
                            emitter.send(SseEmitter.event()
                                    .data("[DONE]")
                                    .id("done")
                                    .name("done"));
                            emitter.complete();
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                        }
                    })
                    .onError(e -> {
                        emitter.completeWithError(e);
                    })
                    .start();
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    @GetMapping("/session")
    public Result<List<ChatSessionDto>> queryChatSessions(Long userId) {
        return sessionSideBarService.queryChatSessions(userId);
    }

    @PostMapping("/session-create")
    public Result<ChatSessionVO> createChatSession(@RequestBody CreateSessionRequest request) {
        return sessionSideBarService.createChatSession(request);
    }

    @GetMapping("/messages")
    public Result<List<ChatMessageDto>> queryHistoryMessages(String memoryId) {
        return chatService.queryHistoryMessages(memoryId);
    }
}
